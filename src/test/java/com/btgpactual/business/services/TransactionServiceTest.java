package com.btgpactual.business.services;

import com.btgpactual.api.dto.TransactionRequestDto;
import com.btgpactual.data.entities.Transaction;
import com.btgpactual.data.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenValidTransactionRequestDto_whenRecordTransaction_thenReturnSavedTransaction() {
        // Given
        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setUserId("user123");
        dto.setFundId("fund456");
        dto.setAmount(new BigDecimal("100.00"));
        dto.setType(Transaction.TransactionType.SUBSCRIPTION);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId("trans789");
        savedTransaction.setUserId(dto.getUserId());
        savedTransaction.setFundId(dto.getFundId());
        savedTransaction.setAmount(dto.getAmount());
        savedTransaction.setType(dto.getType());
        savedTransaction.setTimestamp(LocalDateTime.now());

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        // When
        Transaction result = transactionService.recordTransaction(dto);

        // Then
        assertNotNull(result);
        assertEquals(savedTransaction.getId(), result.getId());
        assertEquals(dto.getUserId(), result.getUserId());
        assertEquals(dto.getFundId(), result.getFundId());
        assertEquals(dto.getAmount(), result.getAmount());
        assertEquals(dto.getType(), result.getType());
        assertNotNull(result.getTimestamp());

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void givenUserId_whenGetUserTransactions_thenReturnOrderedTransactionList() {
        // Given
        String userId = "user123";
        List<Transaction> expectedTransactions = Arrays.asList(
                createTransaction("trans1", userId, LocalDateTime.now()),
                createTransaction("trans2", userId, LocalDateTime.now().minusDays(1))
        );

        when(transactionRepository.findByUserIdOrderByTimestampDesc(userId)).thenReturn(expectedTransactions);

        // When
        List<Transaction> result = transactionService.getUserTransactions(userId);

        // Then
        assertNotNull(result);
        assertEquals(expectedTransactions.size(), result.size());
        assertEquals(expectedTransactions, result);

        verify(transactionRepository).findByUserIdOrderByTimestampDesc(userId);
    }

    @Test
    void givenNullTransactionRequestDto_whenRecordTransaction_thenThrowException() {
        // Given
        TransactionRequestDto dto = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> transactionService.recordTransaction(dto));
    }

    private Transaction createTransaction(String id, String userId, LocalDateTime timestamp) {
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setUserId(userId);
        transaction.setTimestamp(timestamp);
        return transaction;
    }
}