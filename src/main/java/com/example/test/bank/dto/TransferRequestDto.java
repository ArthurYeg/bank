package com.example.test.bank.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequestDto {
    @NotNull
    private Long sourceCardId;

    @NotNull
    private Long targetCardId;

    @Positive
    private BigDecimal amount;
}