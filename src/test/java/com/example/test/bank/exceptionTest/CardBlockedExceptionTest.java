package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.CardBlockedException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardBlockedExceptionTest {

    @Test
    void testCardBlockedExceptionMessage() {
        // Arrange
        String expectedMessage = "Card is blocked";
        CardBlockedException exception = new CardBlockedException(expectedMessage); // Provide the message here

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testCardBlockedExceptionWithoutMessage() {
        // Arrange
        CardBlockedException exception = new CardBlockedException(); // No message provided

        // Act
        String actualMessage = exception.getMessage();

        // Assert
        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
