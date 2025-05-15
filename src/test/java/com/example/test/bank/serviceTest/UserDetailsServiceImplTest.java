package com.example.test.bank.serviceTest;

import com.example.test.bank.model.Role;
import com.example.test.bank.model.User;
import com.example.test.bank.repository.UserRepository;
import com.example.test.bank.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "encodedPassword";

    private User createTestUser(String username, String password, Role role, boolean active) {
        return User.builder()
                .username(username)
                .password(password)
                .roles(new HashSet<>(Set.of(role)))
                .active(active)
                .build();
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExistsAndActive() {

        User mockUser = User.builder()
                .username(TEST_USERNAME)
                .password(TEST_PASSWORD)
                .roles(new HashSet<>(Set.of(Role.USER)))
                .active(true)
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(mockUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(TEST_USERNAME);

        assertNotNull(userDetails);
        assertEquals(TEST_USERNAME, userDetails.getUsername());
        assertEquals(TEST_PASSWORD, userDetails.getPassword());
        assertTrue(userDetails.isEnabled(), "User should be enabled when active=true");

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());


        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserIsNotActive() {

        User inactiveUser = createTestUser(TEST_USERNAME, TEST_PASSWORD, Role.USER, false);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(inactiveUser));

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(TEST_USERNAME);
        });
        assertEquals("User is not active: " + TEST_USERNAME, exception.getMessage());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());


        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("unknown");
        });
        assertEquals("User not found: unknown", exception.getMessage());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUsernameIsEmpty() {

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });
        assertEquals("Username cannot be empty", exception.getMessage());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUsernameIsNull() {

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });
        assertEquals("Username cannot be empty", exception.getMessage());
    }
}