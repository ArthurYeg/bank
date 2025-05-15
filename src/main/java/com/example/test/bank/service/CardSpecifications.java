package com.example.test.bank.service;

import com.example.test.bank.model.Card;
import com.example.test.bank.model.CardStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface CardSpecifications {

    public static Specification<Card> withOwner(String owner) {
        if (owner == null || owner.isEmpty()) {
            throw new IllegalArgumentException("Owner cannot be null or empty");
        }
        return (root, query, cb) -> cb.equal(root.get("cardHolderName"), owner);
    }

    public static Specification<Card> withStatus(String status) {
        try {
            CardStatus cardStatus = CardStatus.valueOf(status.toUpperCase());
            return (root, query, cb) -> cb.equal(root.get("status"), cardStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid card status: " + status);
        }
    }

    public static Specification<Card> createdBetween(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
        return (root, query, cb) -> cb.between(root.get("createdAt"), start, end);
    }

}
