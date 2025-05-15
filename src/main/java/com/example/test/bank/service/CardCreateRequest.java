package com.example.test.bank.service;

import java.time.LocalDate;

public interface CardCreateRequest {
    String getCardholderName();
    String getCardNumber();
    LocalDate getExpirationDate();
    String getCvv();

    void setCardholderName(String janeDoe);

    void setCardNumber(String s);

    void setExpirationDate(LocalDate newDate);

    void setCvv(String number);
}
