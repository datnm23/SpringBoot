package com.github.datnm23.acountservice.resource;

import com.github.datnm23.acountservice.client.UserClient;
import com.github.datnm23.acountservice.exception.UserNotFoundException;
import com.github.datnm23.acountservice.model.request.UserSearchRequest;
import com.github.datnm23.acountservice.model.response.CommonSearchResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.model.response.UserSearchResponse;
import feign.FeignException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserResource {

    UserClient userClient;

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserResponse getDetail(@PathVariable UUID id) throws UserNotFoundException {
        try {
            return userClient.getUserById(id);
        } catch (FeignException.NotFound e) {
            throw new UserNotFoundException("User not found in security service");
        } catch (FeignException e) {
            // Handle other Feign exceptions
            throw new RuntimeException("Error fetching user from security service", e);
        }
    }

    @Operation(summary = "Search users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search completed successfully")
    })
    @GetMapping("/search")
    public CommonSearchResponse<UserSearchResponse> search(UserSearchRequest request) {
        return userClient.searchUsers(request);
    }
}