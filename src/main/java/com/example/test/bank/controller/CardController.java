package com.example.test.bank.controller;

import com.example.test.bank.dto.CardDto;
import com.example.test.bank.dto.CardFilterRequestDto;
import com.example.test.bank.dto.CreateCardRequestDto;
import com.example.test.bank.dto.TransferRequestDto;
import com.example.test.bank.dto.CardResponseDto;
import com.example.test.bank.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor

public class CardController {

    private final CardService cardService;
    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    @GetMapping
    @Operation(summary = "Get all cards (Admin) or user's cards (User)")
    public Page<CardResponseDto> getAllCards(
            @ParameterObject CardFilterRequestDto filter,
            @ParameterObject @PageableDefault Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Fetching cards for user: {}", userDetails.getUsername());
        return cardService.getAllCards(filter, pageable, userDetails);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new card (Admin only)")
    public CardDto createCard(@RequestBody @Valid CreateCardRequestDto request) {
        log.info("Creating new card with request: {}", request);
        return cardService.createCard(request);
    }

    @PostMapping("/{cardId}/block")
    @Operation(summary = "Block card (Admin can block any, User only own)")
    public CardResponseDto blockCard(@PathVariable Long cardId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Blocking card with ID: {} for user: {}", cardId, userDetails.getUsername());
        return cardService.blockCard(cardId, userDetails);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer between user's cards")
    public void transfer(@RequestBody @Valid TransferRequestDto request,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Transferring between cards for user: {}", userDetails.getUsername());
        cardService.transfer(request, userDetails);
    }
}
