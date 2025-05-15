package com.example.test.bank.exception;

import com.example.test.bank.model.CardStatus;

public class InvalidCardOperationException extends RuntimeException {
    private final CardStatus currentStatus;

    public InvalidCardOperationException(String message, CardStatus currentStatus) {
        super(message);
        this.currentStatus = currentStatus;
    }

    public CardStatus getCurrentStatus() {
        return currentStatus;
    }
}