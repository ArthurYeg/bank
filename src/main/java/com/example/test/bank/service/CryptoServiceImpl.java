package com.example.test.bank.service.impl;

import com.example.test.bank.exception.EncryptionException;
import com.example.test.bank.exception.UnsupportedCurrencyException;
import com.example.test.bank.service.CryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Random;

@Service
public class CryptoServiceImpl implements CryptoService {

    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int PBKDF2_ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;

    private final String secretKey;

    public CryptoServiceImpl(@Value("${encryption.secret}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency)
            throws UnsupportedCurrencyException {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        switch (fromCurrency.toUpperCase()) {
            case "USD":
                if (toCurrency.equalsIgnoreCase("EUR")) {
                    return amount.multiply(new BigDecimal("0.85")).setScale(2, RoundingMode.HALF_UP);
                } else if (toCurrency.equalsIgnoreCase("GBP")) {
                    return amount.multiply(new BigDecimal("0.75")).setScale(2, RoundingMode.HALF_UP);
                }
                break;
            case "EUR":
                if (toCurrency.equalsIgnoreCase("USD")) {
                    return amount.multiply(new BigDecimal("1.18")).setScale(2, RoundingMode.HALF_UP);
                }
                break;
            // Add more currency conversions here
        }
        throw new UnsupportedCurrencyException(toCurrency);
    }


    @Override
    public BigDecimal getCurrentPrice(String currency) {
        switch (currency.toUpperCase()) {
            case "BTC": return new BigDecimal("40000");
            case "ETH": return new BigDecimal("3000");
            default: throw new UnsupportedCurrencyException(currency);
        }
    }

    @Override
    public List<BigDecimal> getHistoricalPrices(String currency, int days) {
        BigDecimal basePrice = getCurrentPrice(currency);
        List<BigDecimal> prices = new ArrayList<>();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < days; i++) {
            BigDecimal fluctuation = new BigDecimal(random.nextDouble() * 2000)
                    .setScale(2, RoundingMode.HALF_UP);
            prices.add(basePrice.add(fluctuation));
        }
        return prices;
    }

    @Override
    public String encrypt(String data) {
        if (data == null) throw new IllegalArgumentException("Data cannot be null");
        if (data.isEmpty()) return "";
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            SecretKeySpec secretKey = generateKey(salt);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            byte[] iv = new byte[12];
            random.nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[salt.length + iv.length + encryptedBytes.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(iv, 0, combined, salt.length, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, salt.length + iv.length, encryptedBytes.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new EncryptionException("Encryption failed", e);
        }
    }

    @Override
    public String decrypt(String encryptedData) {
        if (encryptedData == null) throw new IllegalArgumentException("Encrypted data cannot be null");
        if (encryptedData.isEmpty()) return "";
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedData);

            int saltLength = 16;
            int ivLength = 12;
            if (combined.length < saltLength + ivLength) {
                throw new DecryptionException("Invalid encrypted data");
            }
            byte[] salt = Arrays.copyOfRange(combined, 0, saltLength);
            byte[] iv = Arrays.copyOfRange(combined, saltLength, saltLength + ivLength);
            byte[] encryptedBytes = Arrays.copyOfRange(combined, saltLength + ivLength, combined.length);

            SecretKeySpec secretKey = generateKey(salt);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("Decryption failed", e);
        }
    }

    private SecretKeySpec generateKey(byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt, PBKDF2_ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    public String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder("4");
        Random random = new Random();

        for (int i = 1; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }

        while (!isValidLuhn(cardNumber.toString())) {
            cardNumber.setLength(1);
            for (int i = 1; i < 16; i++) {
                cardNumber.append(random.nextInt(10));
            }
        }

        return cardNumber.toString();
    }
    public boolean isValidLuhn(String cardNumber) {
        int sum = 0;
        boolean alternate = false;

        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            alternate = !alternate;
        }

        return (sum % 10 == 0);
    }
    public boolean isValidCardNumber(String cardNumber) {
        return isValidLuhn(cardNumber);
    }

    public int calculateLuhnCheckDigit(String number) {
        int sum = 0;
        boolean isSecond = false;

        // Traverse from the end to the beginning of the number
        for (int i = number.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(number.charAt(i));

            // If this is the second digit from the right, double it
            if (isSecond) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9; // Subtract 9 if the result is greater than 9
                }
            }

            sum += digit; // Add the digit to the sum
            isSecond = !isSecond; // Toggle the flag
        }

        // Calculate the check digit
        return (10 - (sum % 10)) % 10;
    }



    public static class DecryptionException extends RuntimeException {
        public DecryptionException(String message) {
            super(message);
        }

        public DecryptionException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
