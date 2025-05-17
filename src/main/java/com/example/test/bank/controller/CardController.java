package com.example.test.bank.controller;

import com.example.test.bank.dto.*;
import com.example.test.bank.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
public class CardController {

    private static final Logger log = LoggerFactory.getLogger(CardController.class);
    private final CardService cardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create new card (Admin only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Card created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<CardDto> createCard(@RequestBody @Valid CreateCardRequestDto request) {
        CardDto createdCard = cardService.createCard(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @PostMapping("/{cardId}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate card", description = "Activate a deactivated card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card activated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "409", description = "Card already active"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<CardResponseDto> activateCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        CardResponseDto activatedCard = cardService.activateCard(cardId, userDetails);
        return ResponseEntity.ok(activatedCard);
    }

    @PostMapping("/{cardId}/block")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Block card", description = "Block an active card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Card blocked successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "409", description = "Card already blocked"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<CardResponseDto> blockCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        CardResponseDto blockedCard = cardService.blockCard(cardId, userDetails);
        return ResponseEntity.ok(blockedCard);
    }

    @DeleteMapping("/{cardId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete card", description = "Permanently delete a card")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Card deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        cardService.deleteCard(cardId, userDetails);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my")
    @Operation(summary = "Get user's cards", description = "Get paginated list of user's cards with filtering")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cards"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Page<CardResponseDto>> getUserCards(
            @ParameterObject CardFilterRequestDto filter,
            @ParameterObject @PageableDefault Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        Page<CardResponseDto> cards = cardService.getUserCards(filter, pageable, userDetails);
        return ResponseEntity.ok(cards);
    }

    @PostMapping("/{cardId}/request-block")
    @Operation(summary = "Request card block", description = "Request blocking of user's own card")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Block request accepted"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<Void> requestCardBlock(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        cardService.requestCardBlock(cardId, userDetails);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer between own cards",
            description = "Transfer funds between user's own cards with validation")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transfer successful"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "409", description = "Transfer error")
    })
    public ResponseEntity<Void> transferBetweenOwnCards(
            @RequestBody @Valid TransferRequestDto request,
            @AuthenticationPrincipal UserDetails userDetails) {
        cardService.transferBetweenOwnCards(request, userDetails);
        return ResponseEntity.noContent().build();
    }

    // Фильтрация и постраничная выдача
    @GetMapping
    @Operation(summary = "Get filtered cards",
            description = "Get paginated and filtered list of cards")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Invalid filter"),
            @ApiResponse(responseCode = "403", description = "Unauthorized")
    })
    public ResponseEntity<Page<CardResponseDto>> getFilteredCards(
            @ParameterObject @Valid CardFilterRequestDto filter,
            @ParameterObject @PageableDefault(size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(cardService.getFilteredCards(filter, pageable, userDetails));
    }

    @GetMapping("/{cardId}/balance")
    @Operation(summary = "Get card balance", description = "Get balance of user's specific card")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Balance retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized access")
    })
    public ResponseEntity<BalanceResponseDto> getCardBalance(
            @PathVariable Long cardId,
            @AuthenticationPrincipal UserDetails userDetails) {
        BalanceResponseDto balance = cardService.getCardBalance(cardId, userDetails);
        return ResponseEntity.ok(balance);
    }
}