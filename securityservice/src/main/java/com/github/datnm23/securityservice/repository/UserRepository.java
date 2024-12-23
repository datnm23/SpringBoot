package com.github.datnm23.securityservice.repository;


import com.github.datnm23.securityservice.entity.User;
import com.github.datnm23.securityservice.statics.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

}