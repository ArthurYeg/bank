package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.UnauthorizedCardAccessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnauthorizedCardAccessExceptionTest {

    @Test
    void testUnauthorizedCardAccessException() {

        Long expectedCardId = 123456789L;
        String expectedUsername = "testUser ";
        UnauthorizedCardAccessException exception = new UnauthorizedCardAccessException(expectedCardId, expectedUsername);

        Long actualCardId = exception.getCardId();
        String actualUsername = exception.getUsername();

        assertEquals(expectedCardId, actualCardId, "Card ID should match the expected Card ID");
        assertEquals(expectedUsername, actualUsername, "Username should match the expected username");
    }
}
