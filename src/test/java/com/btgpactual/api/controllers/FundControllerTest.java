package com.btgpactual.api.controllers;

import com.btgpactual.api.dto.FundDto;
import com.btgpactual.api.dto.TransactionResponseDto;
import com.btgpactual.business.services.FundService;
import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FundControllerTest {

    @Mock
    private FundService fundService;

    @InjectMocks
    private FundController fundController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenFunds_whenGetAllFunds_thenReturnFundDtoList() {
        // Given
        Fund fund1 = new Fund("1", "Fund 1", BigDecimal.valueOf(1000), "Category 1");
        Fund fund2 = new Fund("2", "Fund 2", BigDecimal.valueOf(2000), "Category 2");
        List<Fund> funds = Arrays.asList(fund1, fund2);
        when(fundService.findAllFunds()).thenReturn(funds);

        // When
        ResponseEntity<List<FundDto>> response = fundController.getAllFunds();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Fund 1", response.getBody().get(0).getName());
        assertEquals("Fund 2", response.getBody().get(1).getName());
        verify(fundService, times(1)).findAllFunds();
    }

    @Test
    void givenFundId_whenCancelSubscription_thenReturnTransactionResponse() {
        // Given
        String fundId = "1";
        Transaction transaction = new Transaction(
                "transactionId",
                "dfsfsgf",
                fundId,
                BigDecimal.valueOf(1000),
                Transaction.TransactionType.CANCELLATION,
                LocalDateTime.now()
        );
        when(fundService.cancelSubscription(fundId)).thenReturn(transaction);

        // When
        ResponseEntity<TransactionResponseDto> response = fundController.cancelSubscription(fundId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(transaction.getId(), Objects.requireNonNull(response.getBody()).getId());
        assertEquals(Transaction.TransactionType.CANCELLATION.toString(), response.getBody().getType());
        verify(fundService, times(1)).cancelSubscription(fundId);
    }

    @Test
    void givenTransactions_whenGetTransactionHistory_thenReturnTransactionResponseDtoList() {
        // Given
        Transaction transaction1 = new Transaction(
                "transactionId1",
                "dfsgeraaeqqq",
                "fundId1",
                BigDecimal.valueOf(1000),
                Transaction.TransactionType.SUBSCRIPTION,
                LocalDateTime.now()
        );
        Transaction transaction2 = new Transaction(
                "transactionId2",
                "fdsvfsbegh",
                "fundId2",
                BigDecimal.valueOf(2000),
                Transaction.TransactionType.CANCELLATION,
                LocalDateTime.now()
        );
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(fundService.getTransactionHistory()).thenReturn(transactions);

        // When
        ResponseEntity<List<TransactionResponseDto>> response = fundController.getTransactionHistory();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());
        assertEquals(transaction1.getId(), response.getBody().get(0).getId());
        assertEquals(Transaction.TransactionType.SUBSCRIPTION.toString(), response.getBody().get(0).getType());
        assertEquals(transaction2.getId(), response.getBody().get(1).getId());
        assertEquals(Transaction.TransactionType.CANCELLATION.toString(), response.getBody().get(1).getType());
        verify(fundService, times(1)).getTransactionHistory();
    }
}