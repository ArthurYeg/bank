package com.example.test.bank.service;

import com.example.test.bank.dto.BalanceResponseDto;
import com.example.test.bank.dto.CardDto;
import com.example.test.bank.dto.CardFilterRequestDto;
import com.example.test.bank.dto.CardResponseDto;
import com.example.test.bank.dto.CreateCardRequestDto;
import com.example.test.bank.dto.TransferRequestDto;
import com.example.test.bank.exception.CardNotFoundException;
import com.example.test.bank.model.Card;
import com.example.test.bank.repository.CardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CardServiceImpl implements CardService{

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
        return cardRepository.save(card);
    }

    @Override
    public Page<CardResponseDto> getAllCards(CardFilterRequestDto filter, Pageable pageable, UserDetails userDetails) {
        return null;
    }

    @Override
    public CardResponseDto createCard(CreateCardRequestDto request, UserDetails userDetails) {
        return null;
    }

    @Override
    public CardDto createCard(CreateCardRequestDto request) {
        return null;
    }

    @Override
    public CardResponseDto blockCard(Long cardId, UserDetails userDetails) {
        return null;
    }

    @Override
    public void transfer(TransferRequestDto request, UserDetails userDetails) {

    }

    @Override
    public Card getCard(long id) {
        return null;
    }

    @Override
    public void deleteCard(long id) {

    }

    @Override
    public void deleteCard(Long cardId, UserDetails userDetails) {

    }

    @Override
    public Card getCard(Long cardId) {
        return null;
    }

    @Override
    public CardDto mapToDto(Card card) {
        return null;
    }

    @Override
    public CardResponseDto activateCard(Long cardId, UserDetails userDetails) {
        return null;
    }

    @Override
    public void transferBetweenOwnCards(TransferRequestDto request, UserDetails userDetails) {

    }

    @Override
    public BalanceResponseDto getCardBalance(Long cardId, UserDetails userDetails) {
        return null;
    }

    @Override
    public Page<CardResponseDto> getUserCards(CardFilterRequestDto filter, Pageable pageable, UserDetails userDetails) {
        return null;
    }

    @Override
    public void requestCardBlock(Long cardId, UserDetails userDetails) {

    }

    @Override
    public Page<CardResponseDto> getFilteredCards(CardFilterRequestDto filter, Pageable pageable, UserDetails userDetails) {
        return null;
    }
}
