package com.example.test.bank.exception;

public class EncryptionException extends RuntimeException {
    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}