package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.CardNotFoundException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CardNotFoundExceptionTest {

    @Test
    void testCardNotFoundExceptionMessage() {

        String expectedMessage = "Card not found";
        CardNotFoundException exception = new CardNotFoundException(expectedMessage);

        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testCardNotFoundExceptionWithoutMessage() {

        CardNotFoundException exception = new CardNotFoundException();

        String actualMessage = exception.getMessage();

        assertEquals("Card not found", actualMessage, "Exception message should match the default message");
    }

    @Test
    void testCardNotFoundExceptionWithId() {

        long cardId = 12345;
        String expectedMessage = "Card not found with ID: " + cardId;
        CardNotFoundException exception = new CardNotFoundException(cardId);

        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message for ID");
    }
}
