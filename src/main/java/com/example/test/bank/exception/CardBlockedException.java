package com.example.test.bank.exception;

public class CardBlockedException extends RuntimeException {
    public CardBlockedException(String message) {
        super(message);
    }

    public CardBlockedException() {
        super();
    }
}
