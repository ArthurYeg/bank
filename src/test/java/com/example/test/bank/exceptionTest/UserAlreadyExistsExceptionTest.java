package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.UserAlreadyExistsException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserAlreadyExistsExceptionTest {

    @Test
    void testUserAlreadyExistsExceptionMessage() {
        // Arrange
        String expectedMessage = "User  already exists";
        UserAlreadyExistsException exception = new UserAlreadyExistsException(expectedMessage); // Provide the message here

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testUserAlreadyExistsExceptionWithoutMessage() {
        // Arrange
        UserAlreadyExistsException exception = new UserAlreadyExistsException(); // No message provided

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
