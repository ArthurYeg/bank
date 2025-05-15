package com.example.test.bank.serviceTest;

import com.example.test.bank.dto.RegisterRequestDto;
import com.example.test.bank.exception.UserNotFoundException;
import com.example.test.bank.model.User;
import com.example.test.bank.model.Role;
import com.example.test.bank.repository.UserRepository;
import com.example.test.bank.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser ;
    private RegisterRequestDto registerRequest;

    @BeforeEach
    void setUp() {
        testUser  = new User();
        testUser .setId(1L);
        testUser .setEmail("test@example.com");
        testUser .setPassword("encodedPassword");
        testUser .setRole(Role.USER);

        registerRequest = new RegisterRequestDto();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
    }

    @Test
    void updateUser_ShouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, new User());
        });
    }

    @Test
    void updatePassword_ShouldUpdatePassword() {
        // Arrange: Mock the behavior of the user repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser ));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");

        userService.updatePassword(1L, "newPassword");

        assertEquals("newEncodedPassword", testUser .getPassword());
        verify(userRepository).save(testUser ); // Verify that save was called
    }

    @Test
    void getById_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User foundUser = userService.getById(1L);

        assertEquals(testUser, foundUser);
    }

    @Test
    void getById_ShouldThrowWhenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getById(1L);
        });
    }

    @Test
    void getByEmail_ShouldReturnUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        User foundUser = userService.getByEmail("test@example.com");

        assertEquals(testUser, foundUser);
    }

    @Test
    void getByEmail_ShouldThrowWhenUserNotFound() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.getByEmail("test@example.com");
        });
    }

    @Test
    void updateUser_ShouldNotUpdatePasswordWhenNull() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setPassword("oldPass");

        User updateData = new User();
        updateData.setEmail("new@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updateData);
        assertEquals("oldPass", result.getPassword());
    }

    @Test
    void updateUser_ShouldHandleEmptyStrings() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setEmail("old@example.com");

        User updateData = new User();
        updateData.setFirstName("");
        updateData.setLastName("");
        updateData.setEmail("");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updateData);

        assertEquals("", result.getFirstName());
        assertEquals("", result.getLastName());
        assertEquals("", result.getEmail());
    }
    @Test
    void updateUser_ShouldUpdateAllFieldsSuccessfully() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("old@example.com");
        existingUser.setPassword("oldPass");
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");
        existingUser.setRole(Role.USER);
        existingUser.setActive(true);

        User updateData = new User();
        updateData.setFirstName("New");
        updateData.setLastName("Name");
        updateData.setEmail("new@example.com");
        updateData.setPassword("newPass");
        updateData.setRole(Role.ADMIN);
        updateData.setActive(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updateData);

        assertNotNull(result.getFirstName(), "FirstName should not be null");
        assertEquals("New", result.getFirstName());
        assertEquals("Name", result.getLastName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("encodedNewPass", result.getPassword());
        assertEquals(Role.ADMIN, result.getRole());
        assertFalse(result.getActive());
        verify(userRepository).save(existingUser);
        verify(passwordEncoder).encode("newPass");
    }


    @Test
    void updateUser_ShouldHandlePartialUpdates() {

        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail("old@example.com");
        existingUser.setFirstName("Old");
        existingUser.setLastName("User");

        User updateData = new User();
        updateData.setFirstName("New");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(1L, updateData);

        assertEquals("New", result.getFirstName());
        assertEquals("old@example.com", result.getEmail());
        assertEquals("User", result.getLastName());
    }


    @Test
    void updateUser_ShouldThrowForNonExistingUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser(1L, new User());
        });
    }

    @Test
    void updateUser_ShouldThrowForNullUser() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(1L, null);
        });
    }

    @Test
    void updateUser_ShouldThrowForNullUserId() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(null, new User());
        });
    }
    @Test
    void updatePassword_ShouldEncodeNewPassword() {
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPass");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

        userService.updatePassword(1L, "newPass");

        assertEquals("encodedNewPass", user.getPassword());
        verify(userRepository).save(user);
    }

}