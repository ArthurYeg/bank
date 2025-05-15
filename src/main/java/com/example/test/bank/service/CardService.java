package com.example.test.bank.service;

import com.example.test.bank.dto.CardDto;
import com.example.test.bank.dto.CardFilterRequestDto;
import com.example.test.bank.dto.CardResponseDto;
import com.example.test.bank.dto.CreateCardRequestDto;
import com.example.test.bank.dto.TransferRequestDto;
import com.example.test.bank.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

public interface CardService {
    Page<CardResponseDto> getAllCards(CardFilterRequestDto filter, Pageable pageable, UserDetails userDetails);
    CardResponseDto createCard(CreateCardRequestDto request, UserDetails userDetails);
    CardDto createCard(CreateCardRequestDto request);
    CardResponseDto blockCard(Long cardId, UserDetails userDetails);
    void transfer(TransferRequestDto request, UserDetails userDetails);
    Card getCard(long id);
    void deleteCard(long id);

    Card getCard(Long cardId);

    CardDto mapToDto(Card card);
}
