package com.example.test.bank.service;

import com.example.test.bank.dto.AuthResponseDto;
import com.example.test.bank.dto.RefreshTokenRequestDto;
import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.dto.UserProfileResponseDto;
import com.example.test.bank.exception.InvalidTokenException;
import com.example.test.bank.exception.UserAlreadyExistsException;
import com.example.test.bank.exception.UserNotActiveException;
import com.example.test.bank.exception.UserNotFoundException;
import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;
import com.example.test.bank.repository.UserRepository;
import com.example.test.bank.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public AuthResponseDto register(RegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            logger.warn("Registration attempt with existing email: {}", request.getEmail());
            throw new UserAlreadyExistsException("Email already in use");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(Role.USER))
                .active(true)
                .build();

        userRepository.save(user);
        logger.info("User  registered successfully: {}", user.getEmail());

        var jwtToken = jwtService.generateToken(user.getEmail(), "ACCESS");
        return new AuthResponseDto(jwtToken, null, user.getEmail(),
                LocalDateTime.now().plusHours(10));
    }
    @Override
    public AuthResponseDto authenticate(String email, String password) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User  not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        var jwtToken = jwtService.generateToken(user.getEmail(), "ACCESS");
        var refreshToken = jwtService.generateToken(user.getEmail(), "REFRESH");

        return new AuthResponseDto(jwtToken, refreshToken, user.getEmail(),
                LocalDateTime.now().plusHours(10));
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequestDto request) {
        final String userEmail = jwtService.extractUsername(request.getRefreshToken());

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (jwtService.isTokenValid(request.getRefreshToken(), (UserDetails) user)
                && jwtService.isTokenType(request.getRefreshToken(), "REFRESH")) {

            var jwtToken = jwtService.generateToken(user.getEmail(), "ACCESS");
            return new AuthResponseDto(
                    jwtToken,
                    request.getRefreshToken(),
                    user.getEmail(),
                    LocalDateTime.now().plusHours(10)
            );
        }
        throw new InvalidTokenException("Invalid refresh token");
    }

    @Override
    public void initiatePasswordReset(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            String resetToken = jwtService.generateToken(user.getEmail(), "PASSWORD_RESET");

        });
    }

    @Override
    public void confirmPasswordReset(String token, String newPassword) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(token, (UserDetails) user)
                || !jwtService.isTokenType(token, "PASSWORD_RESET")) {
            throw new InvalidTokenException("Invalid or expired password reset token");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void changePassword(String email, String oldPassword, String newPassword) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, oldPassword)
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid old password");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void activateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.isActive()) {
            throw new UserNotActiveException("User already active");
        }
        user.setActive(true);
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!user.isActive()) {
            throw new UserNotActiveException("User already inactive");
        }
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public void updateUserRoles(String email, Set<Role> newRoles) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setRoles(newRoles);
        userRepository.save(user);
    }

    @Override
    public UserProfileResponseDto getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("User from DB: username=" + user.getUsername()
                + ", email=" + user.getEmail());

        Set<String> roles = user.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.toSet());

        return UserProfileResponseDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .active(user.isActive())
                .build();
    }

    @Override
    public void validateToken(String token) {
        String email = jwtService.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(token, (UserDetails) user)) {
            throw new InvalidTokenException("Invalid token");
        }

        if (!user.isActive()) {
            throw new UserNotActiveException("User is not active");
        }
    }
}