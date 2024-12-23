package com.github.datnm23.acountservice.service;

import com.github.datnm23.acountservice.client.UserClient;
import com.github.datnm23.acountservice.exception.ObjectNotFoundException;
import com.github.datnm23.acountservice.model.request.UserSearchRequest;
import com.github.datnm23.acountservice.model.response.CommonSearchResponse;
import com.github.datnm23.acountservice.model.response.UserResponse;
import com.github.datnm23.acountservice.model.response.UserSearchResponse;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class UserService {
    UserClient userClient;

    public UserResponse getUserDetail(UUID id) throws ObjectNotFoundException {
        try {
            return userClient.getUserById(id);
        } catch (FeignException.NotFound e) {
            throw new ObjectNotFoundException("User not found");
        } catch (FeignException e) {
            throw new RuntimeException("Error fetching user from security service", e);
        }
    }

    public CommonSearchResponse<UserSearchResponse> searchUsers(UserSearchRequest request) {
        return userClient.searchUsers(request);
    }

}
