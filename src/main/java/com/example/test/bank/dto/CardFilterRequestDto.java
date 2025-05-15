package com.example.test.bank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class CardFilterRequestDto {
    private String cardType;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal balance;
    @NotBlank
    @CreditCardNumber
    private String cardNumber;

    @NotBlank
    @Size(min = 3, max = 50)
    private String cardHolderName;

    public CardFilterRequestDto() {
    }

    public CardFilterRequestDto(String cardType, String status, LocalDate startDate, LocalDate endDate) {
        this.cardType = cardType;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCardHolder() {
        return "";
    }
}
