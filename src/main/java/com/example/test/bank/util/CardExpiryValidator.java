package com.example.test.bank.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;

public class CardExpiryValidator implements ConstraintValidator<ValidExpiry, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        try {
            String[] parts = value.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]) + 2000;

            YearMonth expiry = YearMonth.of(year, month);
            return expiry.isAfter(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }
}