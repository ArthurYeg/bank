package com.example.test.bank.exception;

public class SameCardTransferException extends RuntimeException {
    public SameCardTransferException(String message) {
        super(message);
    }
}
