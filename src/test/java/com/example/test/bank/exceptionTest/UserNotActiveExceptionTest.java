package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.UserNotActiveException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserNotActiveExceptionTest {

    @Test
    void testUserNotActiveExceptionMessage() {
        // Arrange
        String expectedMessage = "User  is not active";
        UserNotActiveException exception = new UserNotActiveException(expectedMessage);

        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testUserNotActiveExceptionWithoutMessage() {

        UserNotActiveException exception = new UserNotActiveException(null);

        String actualMessage = exception.getMessage();

        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }
}
