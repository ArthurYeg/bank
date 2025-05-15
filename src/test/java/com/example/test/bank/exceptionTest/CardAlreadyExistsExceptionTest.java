package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.CardAlreadyExistsException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardAlreadyExistsExceptionTest {

    @Test
    void testCardAlreadyExistsExceptionMessage() {
        // Arrange
        String expectedMessage = "Card already exists";
        CardAlreadyExistsException exception = new CardAlreadyExistsException(expectedMessage); // Provide the message here

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testCardAlreadyExistsExceptionWithoutMessage() {
        // Arrange
        CardAlreadyExistsException exception = new CardAlreadyExistsException(); // No message provided

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
