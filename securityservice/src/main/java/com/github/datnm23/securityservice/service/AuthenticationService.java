package com.github.datnm23.securityservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.datnm23.securityservice.entity.RefreshToken;
import com.github.datnm23.securityservice.entity.Role;
import com.github.datnm23.securityservice.entity.User;
import com.github.datnm23.securityservice.exception.ExistedUserException;
import com.github.datnm23.securityservice.exception.InvalidRefreshTokenException;
import com.github.datnm23.securityservice.exception.ObjectNotFoundException;
import com.github.datnm23.securityservice.model.request.LoginRequest;
import com.github.datnm23.securityservice.model.request.RefreshTokenRequest;
import com.github.datnm23.securityservice.model.request.RegistrationRequest;
import com.github.datnm23.securityservice.model.response.JwtResponse;
import com.github.datnm23.securityservice.model.response.UserResponse;
import com.github.datnm23.securityservice.repository.RefreshTokenRepository;
import com.github.datnm23.securityservice.repository.RoleRepository;
import com.github.datnm23.securityservice.repository.UserRepository;
import com.github.datnm23.securityservice.security.CustomUserDetails;
import com.github.datnm23.securityservice.security.JwtService;
import com.github.datnm23.securityservice.security.SecurityUtils;
import com.github.datnm23.securityservice.statics.Constant;
import com.github.datnm23.securityservice.statics.Roles;
import com.github.datnm23.securityservice.statics.UserStatus;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {

    final JwtService jwtService;

    final UserRepository userRepository;

    final RefreshTokenRepository refreshTokenRepository;

    final AuthenticationManager authenticationManager;

    final RoleRepository roleRepository;

    final PasswordEncoder passwordEncoder;

    final ObjectMapper objectMapper;

    final EmailService emailService;

    @Value("${application.security.refreshToken.tokenValidityMilliseconds}")
    long refreshTokenValidityMilliseconds;

    public UserResponse registerUser(RegistrationRequest registrationRequest)
            throws ObjectNotFoundException, ExistedUserException, MessagingException {
        Optional<User> userOptional = userRepository.findByEmailAndStatus(registrationRequest.getUsername(), UserStatus.ACTIVATED);
        if (userOptional.isPresent()) {
            throw new ExistedUserException("email existed");
        }
        Role role = roleRepository.findByName(Roles.USER)
                .orElseThrow(() -> new ObjectNotFoundException("Cannot find USER role"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User user = User.builder()
                .email(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .roles(roles)
                .status(UserStatus.CREATED)
                .createdBy(Constant.DEFAULT_CREATOR)
                .lastModifiedBy(Constant.DEFAULT_CREATOR)
                .activationMailSentAt(LocalDateTime.now())
                .activationMailSentCount(1)
                .build();
        userRepository.save(user);

        emailService.sendActivationMail(user);

        return objectMapper.convertValue(user, UserResponse.class);
    }

    public JwtResponse authenticate(LoginRequest request) throws ObjectNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtService.generateJwtToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

//        String refreshToken = UUID.randomUUID().toString();
        String refreshToken = jwtService.generateJwtRefreshToken(authentication);
        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .refreshToken(refreshToken)
                .user(user)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);

        return JwtResponse.builder()
                .jwt(jwt)
                .refreshToken(refreshToken)
                .id(userDetails.getId())
                .email(userDetails.getUsername())
                .roles(roles)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public JwtResponse refreshToken(RefreshTokenRequest request) throws InvalidRefreshTokenException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        JwtResponse response = userRepository.findById(userDetails.getId())
                .flatMap(user -> refreshTokenRepository
                        .findByUserAndRefreshTokenAndInvalidated(user, request.getRefreshToken(), false)
                        .map(oldRefreshToken -> {
                            LocalDateTime createdDateTime = oldRefreshToken.getCreatedAt();
                            LocalDateTime expiryTime = createdDateTime.plusSeconds(refreshTokenValidityMilliseconds / 1000);
                            if (expiryTime.isBefore(LocalDateTime.now())) {
                                oldRefreshToken.setInvalidated(true);
                                refreshTokenRepository.save(oldRefreshToken);
                                return null;
                            }
                            String jwtToken = jwtService.generateJwtToken(authentication);
                            String refreshToken = jwtService.generateJwtRefreshToken(authentication);
                            RefreshToken refreshTokenEntity = RefreshToken.builder()
                                    .refreshToken(refreshToken)
                                    .user(user)
                                    .build();
                            refreshTokenRepository.save(refreshTokenEntity);
                            oldRefreshToken.setInvalidated(true);
                            refreshTokenRepository.save(oldRefreshToken);
                            return JwtResponse.builder()
                                    .jwt(jwtToken)
                                    .refreshToken(refreshToken)
                                    .id(userDetails.getId())
                                    .email(userDetails.getUsername())
                                    .roles(userDetails.getAuthorities().stream()
                                            .map(GrantedAuthority::getAuthority)
                                            .collect(Collectors.toSet()))
                                    .build();
                        }))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (response == null) {
            throw new InvalidRefreshTokenException("Refresh token invalid or expired");
        }
        return response;
    }

    @Transactional
    public void logout() {
        UUID userId = SecurityUtils.getCurrentUserLoginId()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        refreshTokenRepository.logOut(userId);
        SecurityContextHolder.clearContext();
    }

}
