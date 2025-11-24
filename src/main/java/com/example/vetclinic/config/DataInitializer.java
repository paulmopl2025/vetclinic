package com.example.vetclinic.config;

import com.example.vetclinic.domain.model.Role;
import com.example.vetclinic.domain.model.User;
import com.example.vetclinic.infrastructure.persistence.RoleJpaRepository;
import com.example.vetclinic.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleJpaRepository roleRepository;
    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Initialize roles
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_ADMIN").build()));

        Role vetRole = roleRepository.findByName("ROLE_VET")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_VET").build()));

        Role recepcionistaRole = roleRepository.findByName("ROLE_RECEPCIONISTA")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_RECEPCIONISTA").build()));

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));

        // Create or update admin user
        createOrUpdateUser("admin", "admin@vetclinic.com", "admin123", Set.of(adminRole));

        // Create or update test users
        createOrUpdateUser("vet1", "maria.gonzalez@vetclinic.com", "password123", Set.of(vetRole));
        createOrUpdateUser("vet2", "carlos.rodriguez@vetclinic.com", "password123", Set.of(vetRole));
        createOrUpdateUser("recepcionista1", "ana.martinez@vetclinic.com", "password123", Set.of(recepcionistaRole));
        createOrUpdateUser("user1", "juan.perez@email.com", "password123", Set.of(userRole));
    }

    private void createOrUpdateUser(String username, String email, String password, Set<Role> roles) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            user = User.builder()
                    .username(username)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .roles(roles)
                    .build();
            userRepository.save(user);
            log.info("User created: {} / {}", username, password);
        } else {
            // Update password if it was created with wrong password from data.sql
            user.setPassword(passwordEncoder.encode(password));
            if (user.getRoles().isEmpty() || !user.getRoles().containsAll(roles)) {
                user.setRoles(roles);
            }
            userRepository.save(user);
            log.info("User updated: {} / {}", username, password);
        }
    }
}
