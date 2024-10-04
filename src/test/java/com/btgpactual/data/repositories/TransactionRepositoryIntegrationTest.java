package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class TransactionRepositoryIntegrationTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Transaction.class);
    }

    @Test
    void givenMultipleTransactions_whenFindByUserIdOrderByTimestampDesc_thenReturnSortedTransactions() {
        // Given
        String userId = "user123";

        Transaction transaction1 = createTransaction(userId, LocalDateTime.now().minusDays(2));
        Transaction transaction2 = createTransaction(userId, LocalDateTime.now());
        Transaction transaction3 = createTransaction(userId, LocalDateTime.now().minusDays(1));

        mongoTemplate.save(transaction1);
        mongoTemplate.save(transaction2);
        mongoTemplate.save(transaction3);

        // When
        List<Transaction> result = transactionRepository.findByUserIdOrderByTimestampDesc(userId);

        // Then
        assertEquals(3, result.size());
        assertEquals(transaction2.getId(), result.get(0).getId());
        assertEquals(transaction3.getId(), result.get(1).getId());
        assertEquals(transaction1.getId(), result.get(2).getId());
    }

    @Test
    void givenNoTransactions_whenFindByUserIdOrderByTimestampDesc_thenReturnEmptyList() {
        // Given
        String userId = "user123";

        // When
        List<Transaction> result = transactionRepository.findByUserIdOrderByTimestampDesc(userId);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenTransactionsFromMultipleUsers_whenFindByUserIdOrderByTimestampDesc_thenReturnOnlyUserTransactions() {
        // Given
        String userId1 = "user123";
        String userId2 = "user456";

        Transaction transaction1 = createTransaction(userId1, LocalDateTime.now().minusDays(1));
        Transaction transaction2 = createTransaction(userId2, LocalDateTime.now());
        Transaction transaction3 = createTransaction(userId1, LocalDateTime.now());

        mongoTemplate.save(transaction1);
        mongoTemplate.save(transaction2);
        mongoTemplate.save(transaction3);

        // When
        List<Transaction> result = transactionRepository.findByUserIdOrderByTimestampDesc(userId1);

        // Then
        assertEquals(2, result.size());
        assertEquals(transaction3.getId(), result.get(0).getId());
        assertEquals(transaction1.getId(), result.get(1).getId());
    }

    private Transaction createTransaction(String userId, LocalDateTime timestamp) {
        Transaction transaction = new Transaction();
        transaction.setUserId(userId);
        transaction.setFundId("fund" + System.nanoTime());  // Unique fund ID
        transaction.setAmount(new BigDecimal("100.00"));
        transaction.setType(Transaction.TransactionType.SUBSCRIPTION);  // Assuming this enum exists
        transaction.setTimestamp(timestamp);
        return mongoTemplate.save(transaction);
    }
}