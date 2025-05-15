package com.example.test.bank.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.time.LocalDate;

public class CreateCardRequestDto {
    @NotBlank
    @Size(min = 16, max = 16)
    @CreditCardNumber
    private String cardNumber;

    @NotBlank
    private String cardOwner;

    @Future
    private LocalDate expirationDate;

    public void setCardNumber(String s) {

    }

    public void setCardHolderName(String testUser) {

    }

    public void setExpiryDate(String s) {

    }

    public void setCvv(String number) {

    }

    public short getCardNumber() {
            return 0;
    }

    public String getCardHolderName() {
        return "";
    }

    public String getExpiryDate() {
        return "";
    }

    public String getCvv() {
        return "";
    }
}
