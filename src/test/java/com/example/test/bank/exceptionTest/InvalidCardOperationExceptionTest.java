package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.InvalidCardOperationException;
import com.example.test.bank.model.CardStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidCardOperationExceptionTest {

    @Test
    void testInvalidCardOperationExceptionMessage() {
        String expectedMessage = "Invalid operation on card";
        CardStatus currentStatus = CardStatus.ACTIVE;
        InvalidCardOperationException exception = new InvalidCardOperationException(expectedMessage, currentStatus);

        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match the expected message");
    }

    @Test
    void testInvalidCardOperationExceptionCurrentStatus() {
        CardStatus expectedStatus = CardStatus.INACTIVE;
        InvalidCardOperationException exception = new InvalidCardOperationException("Invalid operation on card", expectedStatus);

        assertEquals(expectedStatus, exception.getCurrentStatus(), "Current status should match the expected status");
    }
}
