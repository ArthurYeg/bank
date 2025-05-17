package com.example.test.bank.repository;

import com.example.test.bank.model.Card;
import com.example.test.bank.model.CardStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CardRepository extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card> {
    @Query("SELECT c FROM Card c WHERE (c.cardHolderName LIKE %:searchTerm% OR c.cardNumber LIKE %:searchTerm%) AND c.status = :status")
    Page<Card> searchCards(
            @Param("searchTerm") String searchTerm,
            @Param("status") CardStatus status,
            Pageable pageable
    );
}