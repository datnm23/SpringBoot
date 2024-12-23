package com.github.datnm23.securityservice.repository;

import com.github.datnm23.securityservice.entity.Role;
import com.github.datnm23.securityservice.statics.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


    public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findByName(Roles name);

}
