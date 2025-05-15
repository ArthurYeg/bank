package com.example.test.bank.service;

import com.example.test.bank.exception.CardNotFoundException;
import com.example.test.bank.model.Card;
import com.example.test.bank.repository.CardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl {

    private final CardRepository cardRepository;

    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card getCardById(Long id) {
        return cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("Card not found with id: " + id));
    }

    public Card createCard(Card card) {
        return cardRepository.save(card);
    }

    public Card updateCard(Long id, Card cardDetails) {
        Card card = getCardById(id);
        card.setBalance(cardDetails.getBalance());
        // обновите другие поля, если это необходимо
        return cardRepository.save(card);
    }
}
