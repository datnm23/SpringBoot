package com.github.datnm23.acountservice.resource;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.datnm23.acountservice.exception.ExistedUserException;
import com.github.datnm23.acountservice.exception.InvalidRefreshTokenException;
import com.github.datnm23.acountservice.exception.ObjectNotFoundException;
import com.github.datnm23.acountservice.model.request.LoginRequest;
import com.github.datnm23.acountservice.model.request.RefreshTokenRequest;
import com.github.datnm23.acountservice.model.request.RegistrationRequest;
import com.github.datnm23.acountservice.model.response.JwtResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.service.AuthenticationService;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/authentications")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationResource {

    AuthenticationService authenticateService;

    @PostMapping("/login")
    public JwtResponse authenticateUser(@Valid @RequestBody LoginRequest request) throws ObjectNotFoundException {
        return authenticateService.authenticate(request);
    }

    @PostMapping("/registration")
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody RegistrationRequest request)
            throws ExistedUserException, ObjectNotFoundException, MessagingException {
        UserResponse userResponse = authenticateService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @PostMapping("/refresh_token")
    public JwtResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request)
            throws InvalidRefreshTokenException {
        return authenticateService.refreshToken(request);
    }

    @PostMapping("/logout")
    public void logout() {
        authenticateService.logout();
    }

}
