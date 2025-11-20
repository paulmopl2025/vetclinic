package com.example.vetclinic.security;

import com.example.vetclinic.domain.model.User;
import com.example.vetclinic.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurity {

    private final UserJpaRepository userRepo;

    public boolean isOwner(Authentication authentication, Long userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return false;
        }

        String username = ((UserDetails) principal).getUsername();
        return userRepo.findByUsername(username)
                .map(User::getId)
                .map(id -> id.equals(userId))
                .orElse(false);
    }
}
