package com.example.test.bank.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String message) {
        super(message);
    }

    public CardNotFoundException() {
        super("Card not found");
    }

    public CardNotFoundException(long cardId) {
        super("Card not found with ID: " + cardId);
    }
}
