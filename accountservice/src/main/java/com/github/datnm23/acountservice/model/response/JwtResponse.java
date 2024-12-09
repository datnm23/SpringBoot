package com.github.datnm23.acountservice.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JwtResponse {

    String jwt;

    String refreshToken;

    UUID id;

    String email;

    Set<String> roles;

    private long accessTokenExpiry;

    private long refreshTokenExpiry;

}
