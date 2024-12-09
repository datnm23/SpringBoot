package com.github.datnm23.acountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.github.datnm23.acountservice.entity.RefreshToken;
import com.github.datnm23.acountservice.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUserAndRefreshTokenAndInvalidated(User user, String refreshToken, boolean invalidated);

    @Modifying
    @Query("update RefreshToken r set r.invalidated = true where r.user.id = :userId")
    void logOut(UUID userId);

}
