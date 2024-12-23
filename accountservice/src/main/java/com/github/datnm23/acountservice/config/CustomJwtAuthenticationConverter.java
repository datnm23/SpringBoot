package com.github.datnm23.acountservice.config;

import com.github.datnm23.acountservice.security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomJwtAuthenticationConverter {

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    public Authentication convert(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            Claims claims = claimsJws.getPayload();
            String email = claims.getSubject();

            Set<String> roles = ((List<?>) claims.get("roles")).stream()
                    .map(Object::toString)
                    .collect(Collectors.toSet());

            UserDetails userDetails = new CustomUserDetails(email, roles);

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
            throw new RuntimeException("invalid JWT");
        }
    }
}