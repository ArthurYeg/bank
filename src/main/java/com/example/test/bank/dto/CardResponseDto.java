package com.example.test.bank.dto;

import java.math.BigDecimal;

public class CardResponseDto {
    private Long id;
    private String cardNumber;
    private String expiryDate;
    private BigDecimal balance; // Измените на BigDecimal
    private String status;

    public CardResponseDto(Long id, String cardNumber, String expiryDate, BigDecimal balance, String status) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.balance = balance;
        this.status = status;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }
}
