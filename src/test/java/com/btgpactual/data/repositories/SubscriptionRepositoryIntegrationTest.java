package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class SubscriptionRepositoryIntegrationTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Subscription.class);
    }

    @Test
    void givenActiveSubscription_whenFindByUserIdAndFundIdAndIsActive_thenReturnSubscription() {
        // Given
        String userId = "user123";
        String fundId = "fund456";
        Subscription activeSubscription = new Subscription();
        activeSubscription.setUserId(userId);
        activeSubscription.setFundId(fundId);
        activeSubscription.setIsActive(true);
        activeSubscription.setAmount(new BigDecimal("100.00"));
        activeSubscription.setSubscriptionDate(LocalDateTime.now());
        mongoTemplate.save(activeSubscription);

        // When
        Optional<Subscription> result = subscriptionRepository.findByUserIdAndFundIdAndIsActive(userId, fundId, true);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        assertEquals(fundId, result.get().getFundId());
        assertTrue(result.get().getIsActive());
    }

    @Test
    void givenInactiveSubscription_whenFindByUserIdAndFundIdAndIsActive_thenReturnEmpty() {
        // Given
        String userId = "user123";
        String fundId = "fund456";
        Subscription inactiveSubscription = new Subscription();
        inactiveSubscription.setUserId(userId);
        inactiveSubscription.setFundId(fundId);
        inactiveSubscription.setIsActive(false);
        inactiveSubscription.setAmount(new BigDecimal("100.00"));
        inactiveSubscription.setSubscriptionDate(LocalDateTime.now());
        mongoTemplate.save(inactiveSubscription);

        // When
        Optional<Subscription> result = subscriptionRepository.findByUserIdAndFundIdAndIsActive(userId, fundId, true);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNoSubscription_whenFindByUserIdAndFundIdAndIsActive_thenReturnEmpty() {
        // Given
        String userId = "user123";
        String fundId = "fund456";

        // When
        Optional<Subscription> result = subscriptionRepository.findByUserIdAndFundIdAndIsActive(userId, fundId, true);

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void givenMultipleSubscriptions_whenFindByUserIdAndFundIdAndIsActive_thenReturnCorrectSubscription() {
        // Given
        String userId = "user123";
        String fundId = "fund456";

        Subscription activeSubscription = new Subscription();
        activeSubscription.setUserId(userId);
        activeSubscription.setFundId(fundId);
        activeSubscription.setIsActive(true);
        activeSubscription.setAmount(new BigDecimal("100.00"));
        activeSubscription.setSubscriptionDate(LocalDateTime.now());
        mongoTemplate.save(activeSubscription);

        Subscription inactiveSubscription = new Subscription();
        inactiveSubscription.setUserId(userId);
        inactiveSubscription.setFundId(fundId);
        inactiveSubscription.setIsActive(false);
        inactiveSubscription.setAmount(new BigDecimal("200.00"));
        inactiveSubscription.setSubscriptionDate(LocalDateTime.now().minusDays(1));
        mongoTemplate.save(inactiveSubscription);

        // When
        Optional<Subscription> result = subscriptionRepository.findByUserIdAndFundIdAndIsActive(userId, fundId, true);

        // Then
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getUserId());
        assertEquals(fundId, result.get().getFundId());
        assertTrue(result.get().getIsActive());
        assertEquals(new BigDecimal("100.00"), result.get().getAmount());
    }
}