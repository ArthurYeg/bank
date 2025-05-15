package com.example.test.bank.serviceTest;

import com.example.test.bank.exception.CardNotFoundException;
import com.example.test.bank.model.Card;
import com.example.test.bank.model.CardStatus;
import com.example.test.bank.repository.CardRepository;
import com.example.test.bank.service.CardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceImplTest {

    @InjectMocks
    private CardServiceImpl cardService;

    @Mock
    private CardRepository cardRepository;

    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card();
        card.setId(1L);
        card.setCardNumber("1234-5678-9012-3456");
        card.setBalance(BigDecimal.valueOf(1000.00));
        card.setStatus(CardStatus.ACTIVE);
    }

    @Test
    void testGetCardById_ExistingCard_ReturnsCard() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

        Card foundCard = cardService.getCardById(1L);

        assertNotNull(foundCard);
        assertEquals("1234-5678-9012-3456", foundCard.getCardNumber());
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCardById_NonExistingCard_ThrowsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.getCardById(1L));
        verify(cardRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateCard_Success() {
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card createdCard = cardService.createCard(card);

        assertNotNull(createdCard);
        assertEquals("1234-5678-9012-3456", createdCard.getCardNumber());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testUpdateCard_Success() {
        when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
        when(cardRepository.save(any(Card.class))).thenReturn(card);

        card.setBalance(BigDecimal.valueOf(500.00));
        Card updatedCard = cardService.updateCard(1L, card);

        assertNotNull(updatedCard);
        assertEquals(BigDecimal.valueOf(500.00), updatedCard.getBalance());
        verify(cardRepository, times(1)).save(card);
    }

    @Test
    void testUpdateCard_NonExistingCard_ThrowsException() {
        when(cardRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CardNotFoundException.class, () -> cardService.updateCard(1L, card));
        verify(cardRepository, times(1)).findById(1L);
    }
}
