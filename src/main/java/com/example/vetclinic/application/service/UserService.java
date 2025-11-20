package com.example.vetclinic.application.service;

import com.example.vetclinic.application.dto.user.UpdateUserDTO;
import com.example.vetclinic.application.dto.user.UserDTO;
import com.example.vetclinic.domain.model.User;
import com.example.vetclinic.infrastructure.mapper.UserMapper;
import com.example.vetclinic.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            if (userRepo.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail())) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        User updatedUser = userRepo.save(user);
        return userMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepo.deleteById(id);
    }
}
