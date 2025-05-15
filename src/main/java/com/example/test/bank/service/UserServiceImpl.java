package com.example.test.bank.service;

import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.exception.UserNotFoundException;
import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;
import com.example.test.bank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(RegisterRequestDto request, Role role) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .active(true)
                .build();
        user.setRole(role);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long userId, User updatedDetails) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (updatedDetails == null) {
            throw new IllegalArgumentException("Updated details cannot be null");
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Обновляем только явно переданные поля
        if (updatedDetails.getFirstName() != null) {
            existingUser.setFirstName(updatedDetails.getFirstName());
        }
        if (updatedDetails.getLastName() != null) {
            existingUser.setLastName(updatedDetails.getLastName());
        }
        if (updatedDetails.getEmail() != null) {
            existingUser.setEmail(updatedDetails.getEmail());
        }
        if (updatedDetails.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedDetails.getPassword()));
        }
        if (updatedDetails.getRole() != null) {
            existingUser.setRole(updatedDetails.getRole());
        }
        if (updatedDetails.getActive() != null) {
            existingUser.setActive(updatedDetails.getActive());
        }

        return userRepository.save(existingUser);
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }
}