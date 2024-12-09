package com.github.datnm23.acountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.datnm23.acountservice.entity.Role;
import com.github.datnm23.acountservice.statics.Roles;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);

}
