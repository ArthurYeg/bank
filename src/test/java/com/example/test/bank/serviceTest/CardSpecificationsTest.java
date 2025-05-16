package com.example.test.bank.serviceTest;

import com.example.test.bank.model.Card;
import com.example.test.bank.model.CardStatus;
import com.example.test.bank.service.CardSpecifications;
import com.example.test.bank.service.CardSpecificationsImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class CardSpecificationsTest {

    @Test
    void testCreatedBetween_ValidDates() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);

        Specification<Card> spec = CardSpecifications.createdBetween(startDate, endDate);
        assertNotNull(spec);
    }

    @Test
    void testCreatedBetween_InvalidDates() {
        LocalDate startDate = LocalDate.of(2023, 12, 31);
        LocalDate endDate = LocalDate.of(2023, 1, 1);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CardSpecifications.createdBetween(startDate, endDate);
        });
        assertEquals("Start date must be before end date", thrown.getMessage());
    }

    @Test
    void testWithStatus_ValidStatus() {
        String validStatus = "ACTIVE";

        Specification<Card> spec = CardSpecifications.withStatus(validStatus);
        assertNotNull(spec);
    }

    @Test
    void testWithStatus_InvalidStatus() {
        String invalidStatus = "INVALID_STATUS";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            CardSpecifications.withStatus(invalidStatus);
        });
        assertEquals("Invalid card status: INVALID_STATUS", thrown.getMessage());
    }

    @Test
    void testWithOwner_ValidOwner() {
        String owner = "owner@example.com";

        Specification<Card> spec = CardSpecifications.withOwner(owner);
        assertNotNull(spec);
    }

    @Test
    void testWithOwner_InvalidOwner() {
        String[] invalidOwners = {null, ""};

        for (String invalidOwner : invalidOwners) {
            IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
                CardSpecifications.withOwner(invalidOwner);
            });
            assertEquals("Owner cannot be null or empty", thrown.getMessage());
        }
    }

    @Test
    public void testWithOwnerAndStatus() {
        String owner = "owner1";
        String status = "active";
        String someValue = "ваше значение"; // Замените на нужное значение

        CardSpecificationsImpl cardSpecifications = new CardSpecificationsImpl(someValue);

        Specification<Card> spec = cardSpecifications.withOwnerAndStatus(owner, status);
        assertNotNull(spec);
    }

    @Test
    public void testCreatedBefore() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        String someValue = "ваше значение"; // Замените на нужное значение
        CardSpecificationsImpl cardSpecifications = new CardSpecificationsImpl(someValue);

        Specification<Card> spec = cardSpecifications.createdBefore(date);
        assertNotNull(spec);
    }

    @Test
    public void testCreatedAfter() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        String someValue = "ваше значение"; // Замените на нужное значение
        CardSpecificationsImpl cardSpecifications = new CardSpecificationsImpl(someValue);

        Specification<Card> spec = cardSpecifications.createdAfter(date);
        assertNotNull(spec);
    }
}
