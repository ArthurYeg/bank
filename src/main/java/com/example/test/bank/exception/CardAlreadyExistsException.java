package com.example.test.bank.exception;

public class CardAlreadyExistsException extends RuntimeException {
    public CardAlreadyExistsException(String message) {
        super(message);
    }

    public CardAlreadyExistsException() {
        super();
    }
}
