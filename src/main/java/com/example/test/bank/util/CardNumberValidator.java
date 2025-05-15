package com.example.test.bank.util;

import java.util.Set;
public class CardNumberValidator {
    private static final Set<String> ALLOWED_BINS = Set.of("4", "5", "34", "37", "6");
    private static final int MIN_LENGTH = 13;
    private static final int MAX_LENGTH = 19;
    private static final String NUMBER_REGEX = "[^0-9]";

    public static boolean isValid(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }

        String cleaned = cardNumber.replaceAll(NUMBER_REGEX, "");

        if (cleaned.length() < MIN_LENGTH || cleaned.length() > MAX_LENGTH) {
            return false;
        }

        if (ALLOWED_BINS.stream().noneMatch(cleaned::startsWith)) {
            return false;
        }

        return passesLuhnCheck(cleaned);
    }

    private static boolean passesLuhnCheck(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));

            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }

            sum += digit;
            alternate = !alternate;
        }

        return sum % 10 == 0;
    }
}