package com.example.test.bank.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.security.SecureRandom;

@Converter
public class CardNumberEncryptor implements AttributeConverter<String, String> {

    @Value("${encryption.key}")
    private String encryptionKey;

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final byte[] KEY = "Your256BitSecretKey".getBytes(StandardCharsets.UTF_8); // Should be 32 bytes for AES-256

    @Override
    public String convertToDatabaseColumn(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            return null;
        }

        try {

            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, "AES"), ivParameterSpec);

            byte[] encrypted = cipher.doFinal(cardNumber.getBytes(StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt card number", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String encryptedCard) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, new byte[12]);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedCard)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt card number", e);
        }
    }
}