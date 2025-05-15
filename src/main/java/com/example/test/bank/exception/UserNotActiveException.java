package com.example.test.bank.exception;

public class UserNotActiveException extends RuntimeException {
    public UserNotActiveException(String message) { super(message); }
}