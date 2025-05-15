package com.example.test.bank.service;

import com.example.test.bank.exception.UnsupportedCurrencyException;
import java.math.BigDecimal;
import java.util.List;

public interface CryptoService {
    BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency)
            throws UnsupportedCurrencyException;
    BigDecimal getCurrentPrice(String currency);
    List<BigDecimal> getHistoricalPrices(String currency, int days);
    String encrypt(String data);
    String decrypt(String encryptedData);
    String generateCardNumber();
}