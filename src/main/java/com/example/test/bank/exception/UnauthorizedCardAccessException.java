package com.example.test.bank.exception;

public class UnauthorizedCardAccessException extends Throwable {
    private final Long cardId;
    private final String username;

    public UnauthorizedCardAccessException(Long cardId, String username) {
        this.cardId = cardId;
        this.username = username;
    }

    public Long getCardId() {
        return cardId;
    }

    public String getUsername() {
        return username;
    }
}
