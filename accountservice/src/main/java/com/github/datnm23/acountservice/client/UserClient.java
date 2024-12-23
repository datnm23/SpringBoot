package com.github.datnm23.acountservice.client;

import com.github.datnm23.acountservice.model.request.CreateUserRequest;
import com.github.datnm23.acountservice.model.request.UserSearchRequest;
import com.github.datnm23.acountservice.model.response.CommonSearchResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.model.response.UserSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "security-service")
public interface UserClient {
    @GetMapping("/api/auth/users/{id}")
    UserResponse getUserById(@PathVariable("id") UUID id);

    @GetMapping("/api/auth/user")
    UserDetails loadUserByEmail(@RequestParam("email") String email);

    @GetMapping("/api/auth/users/search")
    CommonSearchResponse<UserSearchResponse> searchUsers(@SpringQueryMap UserSearchRequest userSearchRequest);

    @PostMapping("/api/auth/users")
    UserResponse createUser(@RequestBody CreateUserRequest request);
}
