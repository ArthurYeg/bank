package com.example.test.bank.service;

import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;

public interface UserService {
    User createUser (RegisterRequestDto request, Role role);
    User updateUser (Long userId, User updatedDetails);
    void updatePassword(Long userId, String newPassword);
    User getById(Long userId);
    User getByEmail(String email);
}
