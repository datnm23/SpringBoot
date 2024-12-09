package com.github.datnm23.acountservice.resource;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.datnm23.acountservice.exception.ExistedUserException;
import com.github.datnm23.acountservice.exception.ObjectNotFoundException;
import com.github.datnm23.acountservice.model.request.CreateUserRequest;
import com.github.datnm23.acountservice.model.request.UserSearchRequest;
import com.github.datnm23.acountservice.model.response.CommonSearchResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResource {

    UserService userService;

    @GetMapping
    public CommonSearchResponse<?> search(UserSearchRequest request) {
        return userService.searchUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getDetail(@PathVariable UUID id) throws ObjectNotFoundException {
        return userService.getDetail(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CreateUserRequest request) throws ExistedUserException {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(userResponse);
    }

}
