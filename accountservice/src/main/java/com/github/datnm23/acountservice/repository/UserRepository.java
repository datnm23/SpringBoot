package com.github.datnm23.acountservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.github.datnm23.acountservice.entity.User;
import com.github.datnm23.acountservice.statics.UserStatus;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndStatus(String email, UserStatus status);

}