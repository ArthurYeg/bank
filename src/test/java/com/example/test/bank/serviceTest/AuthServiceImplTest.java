package com.example.test.bank.serviceTest;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import com.example.test.bank.exception.InvalidTokenException;
import com.example.test.bank.exception.UserNotActiveException;
import com.example.test.bank.service.UserService;
import org.junit.jupiter.api.Test;

import com.example.test.bank.dto.AuthResponseDto;
import com.example.test.bank.dto.RefreshTokenRequestDto;
import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.dto.UserProfileResponseDto;
import com.example.test.bank.exception.UserAlreadyExistsException;
import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;
import com.example.test.bank.repository.UserRepository;
import com.example.test.bank.security.JwtService;
import com.example.test.bank.service.AuthServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    private Authentication createMockAuthentication() {
        return new Authentication() {
            @Override public Collection<? extends GrantedAuthority> getAuthorities() {
                return Set.of(new SimpleGrantedAuthority("ROLE_USER"));
            }
            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Object getPrincipal() { return mock(UserDetails.class); }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean isAuthenticated) {}
            @Override public String getName() { return "user@example.com"; }
        };
    }

    @Test
    void register_EmailAlreadyExists_ThrowsException() {
        RegisterRequestDto request = new RegisterRequestDto("user", "user@example.com", "password");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }
    @Test
    void register_NewUser_SavesUser_WithEncodedPassword() {
        RegisterRequestDto request = new RegisterRequestDto("user@example.com", "user", "password");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(jwtService.generateToken(request.getEmail(), "ACCESS")).thenReturn("jwtToken");

        AuthResponseDto response = authService.register(request);

        verify(userRepository).save(argThat(user ->
                user.getUsername().equals("user") &&
                        user.getEmail().equals("user@example.com") &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getRoles().contains(Role.USER) &&
                        user.isActive()
        ));

        assertEquals("jwtToken", response.getAccessToken());
    }

    @Test
    void authenticate_UserNotFound_ThrowsException() {
        String email = "nonexistent@example.com";
        String password = "password";

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.authenticate(email, password);
        });
    }

    @Test
    void deactivateUser_UserActive_DeactivatesUser () {
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authService.deactivateUser (email);

        assertFalse(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void initiatePasswordReset_ValidEmail_GeneratesResetToken() {
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(email, "PASSWORD_RESET")).thenReturn("resetToken");

        authService.initiatePasswordReset(email);
    }

    @Test
    void refreshToken_ValidToken_ReturnsNewAccessToken() {
        String refreshToken = "refresh";
        String email = "user@example.com";

        User user = User.builder()
                .username("user")
                .email(email)
                .password("password")
                .active(true)
                .roles(Set.of(Role.USER))
                .build();

        when(jwtService.extractUsername(refreshToken)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refreshToken, user)).thenReturn(true);
        when(jwtService.isTokenType(refreshToken, "REFRESH")).thenReturn(true);
        when(jwtService.generateToken(email, "ACCESS")).thenReturn("newAccess");

        AuthResponseDto response = authService.refreshToken(new RefreshTokenRequestDto(refreshToken));

        assertEquals("newAccess", response.getAccessToken());
        assertEquals(refreshToken, response.getRefreshToken());
    }

    @Test
    void confirmPasswordReset_ValidToken_UpdatesPassword() {
        String token = "validToken";
        String newPassword = "newPass";
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("oldEncoded");
        user.setActive(true);
        user.setRoles(Set.of(Role.USER));

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, user)).thenReturn(true);
        when(jwtService.isTokenType(token, "PASSWORD_RESET")).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn("newEncoded");

        authService.confirmPasswordReset(token, newPassword);

        assertEquals("newEncoded", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    void changePassword_InvalidOldPassword_ThrowsException() {
        String email = "user@example.com";
        String oldPassword = "wrong";
        String newPassword = "newPass";

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager).authenticate(any());

        Exception exception = assertThrows(BadCredentialsException.class,
                () -> authService.changePassword(email, oldPassword, newPassword));

        assertEquals("Invalid old password", exception.getMessage());
    }

    @Test
    void activateUser_UserInactive_ActivatesUser() {
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authService.activateUser(email);

        assertTrue(user.isActive());
        verify(userRepository).save(user);
    }

    @Test
    void getUserProfile_ValidUser_ReturnsProfile() {
        String email = "user@example.com";
        User user = User.builder()
                .username("user")
                .email(email)
                .roles(Set.of(Role.USER))
                .active(true)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserProfileResponseDto response = authService.getUserProfile(email);

        assertEquals("user", response.getUsername());
        assertEquals(Set.of("USER"), response.getRoles());
        assertTrue(response.isActive());
    }
    @Test
    void updateUserRoles_UserFound_UpdatesRoles() {
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .roles(Set.of(Role.USER))
                .build();

        Set<Role> newRoles = Set.of(Role.ADMIN);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        authService.updateUserRoles(email, newRoles);

        assertEquals(newRoles, user.getRoles());
        verify(userRepository).save(user);
    }
    @Test
    void updateUserRoles_UserNotFound_ThrowsException() {
        String email = "nonexistent@example.com";
        Set<Role> newRoles = Set.of(Role.ADMIN);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.updateUserRoles(email, newRoles);
        });
    }
    @Test
    void initiatePasswordReset_UserFound_GeneratesResetToken() {
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(email, "PASSWORD_RESET")).thenReturn("resetToken");

        authService.initiatePasswordReset(email);

        // Verify that the token generation is called
        verify(jwtService).generateToken(email, "PASSWORD_RESET");
    }

//    @Test
//    void initiatePasswordReset_UserNotFound_ThrowsException() {
//        String email = "nonexistent@example.com";
//
//        assertThrows(UsernameNotFoundException.class, () -> {
//            authService.initiatePasswordReset(email);
//        });
//    }
    @Test
    void validateToken_InvalidToken_ThrowsException() {
        String token = "invalidToken";
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, (UserDetails) user)).thenReturn(false);

        assertThrows(InvalidTokenException.class, () -> {
            authService.validateToken(token);
        });
    }

    @Test
    void validateToken_UserInactive_ThrowsException() {
        String token = "validToken";
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, (UserDetails) user)).thenReturn(true);

        assertThrows(UserNotActiveException.class, () -> {
            authService.validateToken(token);
        });
    }

    @Test
    void validateToken_ValidTokenAndActiveUser_Succeeds() {
        String token = "valid";
        String email = "user@example.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, (UserDetails) user)).thenReturn(true);

        assertDoesNotThrow(() -> authService.validateToken(token));
    }
    @Test
    public void testValidateToken() {
        // Arrange
        String token = "validToken";
        String email = "user@example.com";
        User user = User.builder().email(email).active(true).build();

        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(token, (UserDetails) user)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> authService.validateToken(token));
    }

    @Test
    public void testGetUserProfile() {
        // Arrange
        String userId = "user123";
        User user = User.builder().email(userId).username("user").active(true).roles(Set.of(Role.USER)).build();
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));

        // Act
        UserProfileResponseDto profile = authService.getUserProfile(userId);

        // Assert
        assertNotNull(profile);
        assertEquals(userId, profile.getEmail());
    }

    @Test
    public void testDeactivateUser () {
        // Arrange
        String userId = "user123";
        User user = User.builder().email(userId).active(true).build();
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));

        // Act
        authService.deactivateUser (userId);
        UserProfileResponseDto profile = authService.getUserProfile(userId);

        // Assert
        assertFalse(profile.isActive());
    }

    @Test
    public void testActivateUser () {
        // Arrange
        String userId = "user123";
        User user = User.builder().email(userId).active(false).build();
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));

        // Act
        authService.activateUser (userId);
        UserProfileResponseDto profile = authService.getUserProfile(userId);

        // Assert
        assertTrue(profile.isActive());
    }

    @Test
    public void testChangePassword() {
        // Arrange
        String userId = "user123";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        User user = User.builder().email(userId).password(passwordEncoder.encode(oldPassword)).active(true).build();
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));

        // Act
        authService.changePassword(userId, oldPassword, newPassword);

        // Assert
        assertEquals(passwordEncoder.encode(newPassword), user.getPassword());
    }

    @Test
    public void testConfirmPasswordReset() {
        // Arrange
        String userId = "user123";
        String newPassword = "newPassword";
        String validToken = "validToken";

        User user = User.builder().email(userId).active(true).build();
        when(userRepository.findByEmail(userId)).thenReturn(Optional.of(user));

        when(jwtService.extractUsername(validToken)).thenReturn(userId);
        when(jwtService.isTokenValid(validToken, (UserDetails) user)).thenReturn(true);
        when(jwtService.isTokenType(validToken, "PASSWORD_RESET")).thenReturn(true);

        authService.confirmPasswordReset(validToken, newPassword);

        assertEquals(passwordEncoder.encode(newPassword), user.getPassword());
    }


//    @Test
//    public void testRefreshToken() {
//        // Arrange
//        String oldToken = "oldToken";
//        when(jwtService.isTokenValid(eq(oldToken), any())).thenReturn(true); // Use eq() for oldToken
//        when(jwtService.generateToken(any(), any())).thenReturn("newToken");
//
//        // Act
//        String newToken = authService.refreshToken(new RefreshTokenRequestDto(oldToken)).getAccessToken();
//
//        // Assert
//        assertNotNull(newToken);
//        assertNotEquals(oldToken, newToken);
//    }
//

}
