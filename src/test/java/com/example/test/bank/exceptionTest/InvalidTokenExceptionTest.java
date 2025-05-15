package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidTokenExceptionTest {

    @Test
    void testInvalidTokenExceptionMessage() {
        // Arrange
        String expectedMessage = "Invalid token provided";
        InvalidTokenException exception = new InvalidTokenException(expectedMessage);

        // Act
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testInvalidTokenExceptionWithoutMessage() {

        InvalidTokenException exception = new InvalidTokenException(null);

        String actualMessage = exception.getMessage();

        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
