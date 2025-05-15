package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.UserNotFoundException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNotFoundExceptionTest {

    @Test
    void testUserNotFoundExceptionMessage() {
        // Arrange
        String expectedMessage = "User  not found";
        UserNotFoundException exception = new UserNotFoundException(expectedMessage); // Provide the message here

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testUserNotFoundExceptionWithoutMessage() {
        // Arrange
        UserNotFoundException exception = new UserNotFoundException(); // No message provided

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
