package com.example.test.bank.controller;

import com.example.test.bank.dto.*;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    @Operation(summary = "Get all cards")
    public Page<CardResponseDto> getAllCards(
            @ParameterObject CardFilterRequestDto filter,
            @ParameterObject @PageableDefault Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return cardService.getAllCards(filter, pageable, userDetails);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new card (Admin only)")
    public CardDto createCard(@RequestBody @Valid CreateCardRequestDto request) {
        return cardService.createCard(request);
    }

    @PostMapping("/{cardId}/block")
    @Operation(summary = "Block card")
    public CardResponseDto blockCard(@PathVariable Long cardId,
                                     @AuthenticationPrincipal UserDetails userDetails) {
        return cardService.blockCard(cardId, userDetails);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer between cards")
    public void transfer(@RequestBody @Valid TransferRequestDto request,
                         @AuthenticationPrincipal UserDetails userDetails) {
        cardService.transfer(request, userDetails);
    }
}