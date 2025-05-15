package com.example.test.bank.exceptionTest;

import com.example.test.bank.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InsufficientFundsExceptionTest {

    @Test
    void testInsufficientFundsExceptionMessage() {
        String expectedMessage = "Insufficient funds";
        InsufficientFundsException exception = new InsufficientFundsException(expectedMessage);
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage, "Exception message should match the expected message");
    }

    @Test
    void testInsufficientFundsExceptionWithoutMessage() {
        InsufficientFundsException exception = new InsufficientFundsException();
        String actualMessage = exception.getMessage();

        assertEquals(null, actualMessage, "Exception message should be null when no message is provided");
    }

    @Test
    void testInsufficientFundsExceptionWithBalanceAndAmount() {
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal requiredAmount = new BigDecimal("150.00");
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds", currentBalance, requiredAmount);

        assertEquals(currentBalance, exception.getCurrentBalance(), "Current balance should match the expected balance");
        assertEquals(requiredAmount, exception.getRequiredAmount(), "Required amount should match the expected amount");
    }
}
