package com.example.test.bank.exception;

public class UnsupportedCurrencyException extends RuntimeException {
    public UnsupportedCurrencyException(String currency) {
        super("Currency not supported: " + currency);
    }
}