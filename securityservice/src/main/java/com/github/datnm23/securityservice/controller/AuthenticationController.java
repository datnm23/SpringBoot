package com.github.datnm23.securityservice.controller;


import com.github.datnm23.securityservice.exception.ExistedUserException;
import com.github.datnm23.securityservice.exception.InvalidRefreshTokenException;
import com.github.datnm23.securityservice.exception.ObjectNotFoundException;
import com.github.datnm23.securityservice.model.request.LoginRequest;
import com.github.datnm23.securityservice.model.request.RefreshTokenRequest;
import com.github.datnm23.securityservice.model.request.RegistrationRequest;
import com.github.datnm23.securityservice.model.response.JwtResponse;
import com.github.datnm23.securityservice.model.response.UserResponse;
import com.github.datnm23.securityservice.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request) throws ObjectNotFoundException {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegistrationRequest request)
            throws ExistedUserException, ObjectNotFoundException, MessagingException {
        UserResponse userResponse = authenticationService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/refresh-token")
    public JwtResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request)
            throws InvalidRefreshTokenException {
        return authenticationService.refreshToken(request);
    }

    @PostMapping("/logout")
    public void logout() {
        authenticationService.logout();
    }
}
