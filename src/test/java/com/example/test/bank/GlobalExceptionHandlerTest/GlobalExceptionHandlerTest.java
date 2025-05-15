package com.example.test.bank.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private com.example.test.bank.exception.GlobalExceptionHandler exceptionHandler;

    @Test
    void handleBankTransactionException_ReturnsBadRequest() {
        String errorMessage = "Insufficient funds";
        BankTransactionException exception = new BankTransactionException(errorMessage);

        ResponseEntity<Object> response =
                exceptionHandler.handleBankTransactionException(exception, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(errorMessage, body.get("message"));
        assertEquals(HttpStatus.BAD_REQUEST.value(), body.get("status"));
    }

    @Test
    void handleAllExceptions_ReturnsInternalServerError() {
        Exception exception = new Exception("Unexpected error");

        ResponseEntity<Object> response =
                exceptionHandler.handleAllExceptions(exception, null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Internal server error", body.get("message"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), body.get("status"));
    }

    @Test
    void handleNotFound_ReturnsNotFound() {
        String errorMessage = "Card not found";
        RuntimeException exception = new CardNotFoundException(errorMessage);

        ResponseEntity<Object> response =
                exceptionHandler.handleNotFound(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(errorMessage, body.get("error"));
    }

    @Test
    void handleInsufficientFunds_ReturnsBadRequest() {
        BigDecimal currentBalance = new BigDecimal("100.00");
        BigDecimal requiredAmount = new BigDecimal("150.00");
        InsufficientFundsException exception = new InsufficientFundsException("Insufficient funds", currentBalance, requiredAmount);

        ResponseEntity<Object> response =
                exceptionHandler.handleInsufficientFunds(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals("Insufficient funds", body.get("error"));
        assertEquals(currentBalance, body.get("currentBalance"));
        assertEquals(requiredAmount, body.get("required"));
    }

    @Test
    void handleAccessDenied_ReturnsForbidden() {
        String errorMessage = "Access denied";
        AccessDeniedException exception = new AccessDeniedException(errorMessage);

        ResponseEntity<Object> response =
                exceptionHandler.handleAccessDenied(exception);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertEquals(errorMessage, body.get("message"));
        assertEquals(HttpStatus.FORBIDDEN.value(), body.get("status"));
    }
}
