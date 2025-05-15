package com.example.test.bank.util;

import java.util.Random;

public class CardNumberGenerator {
    private static final Random random = new Random();
    private static final String[] PREFIXES = {"4", "5", "34", "37"};

    public static String generate() {
        String prefix = PREFIXES[random.nextInt(PREFIXES.length)];
        String base = prefix + String.format("%015d", random.nextInt(1_000_000_000));
        return formatWithLuhnCheck(base);
    }

    private static String formatWithLuhnCheck(String base) {
        int checkDigit = calculateLuhnCheckDigit(base);
        String fullNumber = base + checkDigit;
        return formatCardNumber(fullNumber);
    }

    private static int calculateLuhnCheckDigit(String number) {

        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) digit -= 9;
            }
            sum += digit;
        }
        return (10 - (sum % 10)) % 10;
    }

    private static String formatCardNumber(String number) {
        return number.replaceAll("(.{4})", "$1 ").trim();
    }
}