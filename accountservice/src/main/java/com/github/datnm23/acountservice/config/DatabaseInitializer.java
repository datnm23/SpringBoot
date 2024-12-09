package com.github.datnm23.acountservice.config;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.datnm23.acountservice.entity.Role;
import com.github.datnm23.acountservice.entity.User;
import com.github.datnm23.acountservice.repository.RoleRepository;
import com.github.datnm23.acountservice.repository.UserRepository;
import com.github.datnm23.acountservice.statics.Constant;
import com.github.datnm23.acountservice.statics.Roles;
import com.github.datnm23.acountservice.statics.UserStatus;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DatabaseInitializer implements CommandLineRunner {

    UserRepository userRepository;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        Optional<Role> roleUserOptional = roleRepository.findByName(Roles.USER);
        if (roleUserOptional.isEmpty()) {
            Role userRole = Role.builder()
                    .id(UUID.randomUUID())  // Manually set the UUID
                    .name(Roles.USER)
                    .build();
            roleRepository.save(userRole);
        }

        Optional<Role> userUserOptional = roleRepository.findByName(Roles.ADMIN);
        if (userUserOptional.isEmpty()) {
            Role adminRole = Role.builder().name(Roles.ADMIN).build();
            roleRepository.save(adminRole);

            Optional<User> admin = userRepository.findByEmail("admin@gmail.com");
            if (admin.isEmpty()) {
                User user = new User();
                user.setEmail("admin");
                user.setPassword(passwordEncoder.encode("admin123")); // Encrypt the password
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                user.setRoles(roles);
                user.setStatus(UserStatus.ACTIVATED);
                user.setCreatedBy(Constant.DEFAULT_CREATOR);
                user.setLastModifiedBy(Constant.DEFAULT_CREATOR);
                userRepository.save(user);
            }
        }
    }

}
