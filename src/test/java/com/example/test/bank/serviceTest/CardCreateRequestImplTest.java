package com.example.test.bank.serviceTest;

import com.example.test.bank.service.CardCreateRequest;
import com.example.test.bank.service.CardCreateRequestImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class CardCreateRequestImplTest {

    private CardCreateRequest request;

    @BeforeEach
    public void setUp() {
        // Initialize the request object before each test
        request = new CardCreateRequestImpl("John Doe", "1234-5678-9012-3456", LocalDate.of(2025, 12, 31), "123");
    }

    @Test
    public void testGetCardholderName() {
        assertEquals("John Doe", request.getCardholderName());
    }

    @Test
    public void testGetCardNumber() {
        assertEquals("1234-5678-9012-3456", request.getCardNumber());
    }

    @Test
    public void testGetExpirationDate() {
        assertEquals(LocalDate.of(2025, 12, 31), request.getExpirationDate());
    }

    @Test
    public void testGetCvv() {
        assertEquals("123", request.getCvv());
    }

    @Test
    public void testSetCardholderName() {
        request.setCardholderName("Jane Doe");
        assertEquals("Jane Doe", request.getCardholderName());
    }

    @Test
    public void testSetCardNumber() {
        request.setCardNumber("9876-5432-1098-7654");
        assertEquals("9876-5432-1098-7654", request.getCardNumber());
    }

    @Test
    public void testSetExpirationDate() {
        LocalDate newDate = LocalDate.of(2026, 12, 31);
        request.setExpirationDate(newDate);
        assertEquals(newDate, request.getExpirationDate());
    }

    @Test
    public void testSetCvv() {
        request.setCvv("456");
        assertEquals("456", request.getCvv());
    }
}
