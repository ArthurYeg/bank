package com.example.test.bank.service;

import com.example.test.bank.dto.*;
import com.example.test.bank.exception.InvalidTokenException;
import com.example.test.bank.exception.UserNotActiveException;
import com.example.test.bank.model.Role;

import java.util.Set;

public interface AuthService {
    AuthResponseDto register(RegisterRequestDto request);
    AuthResponseDto authenticate(String email, String password);
    AuthResponseDto refreshToken(RefreshTokenRequestDto request);
    void initiatePasswordReset(String email);
    void confirmPasswordReset(String token, String newPassword) throws InvalidTokenException;
    void changePassword(String email, String oldPassword, String newPassword);
    void activateUser(String email) throws UserNotActiveException;
    void deactivateUser(String email) throws UserNotActiveException;
    void updateUserRoles(String email, Set<Role> newRoles);
    UserProfileResponseDto getUserProfile(String email);
    void validateToken(String token) throws InvalidTokenException, UserNotActiveException;
}