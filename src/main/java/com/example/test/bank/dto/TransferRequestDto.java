package com.example.test.bank.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {

    @NotNull(message = "Source card ID must not be null")
    @Positive(message = "Invalid card ID")
    private Long fromCardId;

    @NotNull(message = "Destination card ID must not be null")
    @Positive(message = "Invalid card ID")
    private Long toCardId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    @AssertTrue(message = "Source and destination cards must be different")
    private boolean isValidCards() {
        return !fromCardId.equals(toCardId);
    }
}