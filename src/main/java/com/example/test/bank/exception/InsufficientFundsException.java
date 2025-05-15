package com.example.test.bank.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    private final BigDecimal currentBalance;
    private final BigDecimal requiredAmount;

    public InsufficientFundsException(String message) {
        super(message);
        this.currentBalance = BigDecimal.ZERO;
        this.requiredAmount = BigDecimal.ZERO;
    }

    public InsufficientFundsException() {
        super();
        this.currentBalance = BigDecimal.ZERO;
        this.requiredAmount = BigDecimal.ZERO;
    }

    public InsufficientFundsException(String message, BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(message);
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
    }

    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }
}
