package com.github.datnm23.acountservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.datnm23.acountservice.dto.SearchUserDto;
import com.github.datnm23.acountservice.entity.Role;
import com.github.datnm23.acountservice.entity.User;
import com.github.datnm23.acountservice.exception.ExistedUserException;
import com.github.datnm23.acountservice.exception.ObjectNotFoundException;
import com.github.datnm23.acountservice.model.request.CreateUserRequest;
import com.github.datnm23.acountservice.model.request.UserSearchRequest;
import com.github.datnm23.acountservice.model.response.CommonSearchResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.model.response.UserSearchResponse;
import com.github.datnm23.acountservice.repository.RoleRepository;
import com.github.datnm23.acountservice.repository.UserRepository;
import com.github.datnm23.acountservice.repository.custom.UserCustomRepository;
import com.github.datnm23.acountservice.statics.Roles;
import com.github.datnm23.acountservice.statics.UserStatus;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService {

    final PasswordEncoder passwordEncoder;

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final ObjectMapper objectMapper;

    final UserCustomRepository userCustomRepository;

    public UserResponse getDetail(UUID id) throws ObjectNotFoundException {
        return userRepository.findById(id)
                .map(u -> objectMapper.convertValue(u, UserResponse.class))
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));
    }


    public UserResponse createUser(CreateUserRequest request) throws ExistedUserException {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            throw new ExistedUserException("email existed");
        }

        Set<Role> roles = roleRepository.findByName(Roles.USER).stream().collect(Collectors.toSet());

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .roles(roles)
                .status(UserStatus.PENDING_ACTIVATION)
                .build();
        userRepository.save(user);
        return objectMapper.convertValue(user, UserResponse.class);
    }

    public CommonSearchResponse<UserSearchResponse> searchUser(UserSearchRequest request) {
        List<SearchUserDto> result = userCustomRepository.searchUser(request);

        Long totalRecord = 0L;
        List<UserSearchResponse> studentResponses = new ArrayList<>();
        if (!result.isEmpty()) {
            totalRecord = result.get(0).getTotalRecord();
            studentResponses = result
                    .stream()
                    .map(s -> objectMapper.convertValue(s, UserSearchResponse.class))
                    .toList();
        }

        int totalPage = (int) Math.ceil((double) totalRecord / request.getPageSize());

        return CommonSearchResponse.<UserSearchResponse>builder()
                .totalRecord(totalRecord)
                .totalPage(totalPage)
                .data(studentResponses)
                .pageInfo(new CommonSearchResponse.CommonPagingResponse(request.getPageSize(), request.getPageIndex()))
                .build();
    }


    public void deleteUser(UUID userId) throws ObjectNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        user.setDeletedDateTime(LocalDateTime.now());
        userRepository.save(user);
    }

}
