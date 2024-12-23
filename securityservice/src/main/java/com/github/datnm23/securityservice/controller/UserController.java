package com.github.datnm23.securityservice.controller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;
    @GetMapping("/search")
    public CommonSearchResponse<UserSearchResponse> search(UserSearchRequest request) {
        return userService.searchUser(request);
    }

    @GetMapping("/{id}")
    public UserResponse getDetail(@PathVariable UUID id) throws ObjectNotFoundException {
        return userService.getDetail(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateUserRequest request) throws ExistedUserException {
        UserResponse userResponse = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(userResponse);
    }
}