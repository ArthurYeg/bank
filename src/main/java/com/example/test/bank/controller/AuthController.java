package com.example.test.bank.controller;

import com.example.test.bank.dto.AuthRequestDto;
import com.example.test.bank.dto.AuthResponseDto;
import com.example.test.bank.dto.RefreshTokenRequestDto;
import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@Data
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthController {
    private String email;
    private String password;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Register new user",
            description = "Creates a new user account and returns authentication tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })
    public ResponseEntity<AuthResponseDto> register(@RequestBody @Valid RegisterRequestDto request) {
        log.info("Registering new user: {}", request.getEmail());
        return new ResponseEntity<>(authService.register(request), HttpStatus.CREATED);
    }

    @PostMapping(
            value = "/authenticate",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Authenticate user",
            description = "Authenticates user and returns JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "403", description = "Account disabled/locked")
    })
    public ResponseEntity<AuthResponseDto> authenticate(@RequestBody @Valid AuthRequestDto request) {
        String ipAddress = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest().getRemoteAddr();
        log.info("Authentication attempt for {} from IP: {}", request.getEmail(), ipAddress);

        // Call the authenticate method with email and password
        AuthResponseDto authResponse = authService.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(authResponse);
    }


    @PostMapping(
            value = "/refresh-token",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Refresh token",
            description = "Generates new access token using refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid/expired refresh token")
    })
    public ResponseEntity<AuthResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto request) {
        log.info("Refreshing token for user");
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/password-reset")
    @Operation(summary = "Request password reset")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Reset request accepted"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.accepted().build();
    }
}