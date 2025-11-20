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

        // Create admin user if not exists
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = User.builder()
                    .username("admin")
                    .email("admin@vetclinic.com")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            log.info("Admin user created: admin / admin123");
        }
    }
}
