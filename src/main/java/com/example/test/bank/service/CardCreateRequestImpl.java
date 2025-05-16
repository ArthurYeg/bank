package com.example.test.bank.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardCreateRequestImpl implements CardCreateRequest {
    private String cardholderName;
    private String cardNumber;
    private LocalDate expirationDate;
    private String cvv;

    public CardCreateRequestImpl(String cardholderName, String cardNumber, LocalDate expirationDate, String cvv) {
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
    }

    @Override
    public String getCardholderName() {
        return cardholderName;
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String getCvv() {
        return cvv;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}
