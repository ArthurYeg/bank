package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.AccessDeniedException; // Adjust the import based on your package structure
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AccessDeniedExceptionTest {

    @Test
    void testAccessDeniedExceptionMessage() {
        String expectedMessage = "Access Denied";
        AccessDeniedException exception = new AccessDeniedException(expectedMessage);

        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testAccessDeniedExceptionWithoutMessage() {

        AccessDeniedException exception = new AccessDeniedException();

        String actualMessage = exception.getMessage();

        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
