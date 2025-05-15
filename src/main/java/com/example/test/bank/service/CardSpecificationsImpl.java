package com.example.test.bank.service;

import com.example.test.bank.model.Card;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class CardSpecificationsImpl {

    public Specification<Card> withOwnerAndStatus(String owner, String status) {
        return Specification.where(CardSpecifications.withOwner(owner))
                .and(CardSpecifications.withStatus(status));
    }

    public Specification<Card> createdAfter(LocalDate date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), date);
    }

    public Specification<Card> createdBefore(LocalDate date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("createdAt"), date);
    }
}
