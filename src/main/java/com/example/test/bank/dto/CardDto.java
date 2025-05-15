package com.example.test.bank.dto;

import java.math.BigDecimal;

public class CardDto {
    private Long id;
    private String cardNumber;
    private String cardHolderName;
    private String expiryDate;
    private BigDecimal balance;
    private String status;
    private String cvv; // Add this field

    // Constructor
    public CardDto(Long id, String cardNumber, String cardHolderName, String expiryDate, BigDecimal balance, String status, String cvv) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.balance = balance;
        this.status = status;
        this.cvv = cvv; // Initialize the cvv field
    }

    // Default constructor
    public CardDto() {}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCvv() {
        return cvv; // Getter for cvv
    }

    public void setCvv(String cvv) {
        this.cvv = cvv; // Setter for cvv
    }
}
