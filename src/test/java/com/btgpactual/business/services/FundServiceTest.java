package com.btgpactual.business.services;

import com.btgpactual.business.exceptions.FundNotFoundException;
import com.btgpactual.business.exceptions.SubscriptionNotFoundException;
import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.entities.Subscription;
import com.btgpactual.data.entities.Transaction;
import com.btgpactual.data.entities.User;
import com.btgpactual.data.repositories.FundRepository;
import com.btgpactual.data.repositories.SubscriptionRepository;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FundServiceTest {

    @InjectMocks
    private FundService fundService;

    @Mock
    private FundRepository fundRepository;
    @Mock
    private UserService userService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void givenExistingFunds_whenFindAllFunds_thenReturnAllFunds() {
        // Given
        List<Fund> expectedFunds = Arrays.asList(
                new Fund("1", "Fund 1", new BigDecimal("100"), "PFV"),
                new Fund("2", "Fund 2", new BigDecimal("200"), "RTF")
        );
        when(fundRepository.findAll()).thenReturn(expectedFunds);

        // When
        List<Fund> actualFunds = fundService.findAllFunds();

        // Then
        assertEquals(expectedFunds, actualFunds);
        verify(fundRepository).findAll();
    }

    @Test
    void givenValidSubscriptionRequest_whenSubscribeTo_thenReturnTransaction() {
        // Given
        String fundId = "1";
        BigDecimal amount = new BigDecimal("500");
        String notificationPreference = "email";

        User user = new User();
        user.setId("userId");
        user.setBalance(new BigDecimal("1000"));

        Fund fund = new Fund(fundId, "Test Fund", new BigDecimal("100"), "PRT");

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId("transactionId");

        when(userService.getUser()).thenReturn(user);
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));
        when(transactionService.recordTransaction(any())).thenReturn(expectedTransaction);

        // When
        Transaction actualTransaction = fundService.subscribeTo(fundId, amount, notificationPreference);

        // Then
        assertEquals(expectedTransaction, actualTransaction);
        assertEquals(User.NotificationType.EMAIL, user.getNotificationPreference());
        verify(userService).updateBalance(user.getId(), amount.negate());
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(notificationService).sendNotification(eq(user), anyString(), anyString());
    }

    @Test
    void givenInvalidFundId_whenSubscribeTo_thenThrowFundNotFoundException() {
        // Given
        String fundId = "invalidId";
        BigDecimal amount = new BigDecimal("500");
        String notificationPreference = "email";

        when(userService.getUser()).thenReturn(new User());
        when(fundRepository.findById(fundId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(FundNotFoundException.class, () ->
                fundService.subscribeTo(fundId, amount, notificationPreference));
    }

    @Test
    void givenValidCancellationRequest_whenCancelSubscription_thenReturnTransaction() {
        // Given
        String fundId = "1";
        User user = new User();
        user.setId("userId");

        Fund fund = new Fund(fundId, "Test Fund", new BigDecimal("100"), "RTF");

        Subscription subscription = new Subscription();
        subscription.setId("subscriptionId");
        subscription.setAmount(new BigDecimal("500"));

        Transaction expectedTransaction = new Transaction();
        expectedTransaction.setId("transactionId");

        when(userService.getUser()).thenReturn(user);
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByUserIdAndFundIdAndIsActive(user.getId(), fundId, true))
                .thenReturn(Optional.of(subscription));
        when(transactionService.recordTransaction(any())).thenReturn(expectedTransaction);

        // When
        Transaction actualTransaction = fundService.cancelSubscription(fundId);

        // Then
        assertEquals(expectedTransaction, actualTransaction);
        verify(userService).updateBalance(user.getId(), subscription.getAmount());
        verify(mongoTemplate).updateFirst(any(Query.class), any(Update.class), eq(Subscription.class));
        verify(notificationService).sendNotification(eq(user), anyString(), anyString());

        // Verificar que la actualización de la suscripción se realizó correctamente
        ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);
        verify(mongoTemplate).updateFirst(any(Query.class), updateCaptor.capture(), eq(Subscription.class));
        Update capturedUpdate = updateCaptor.getValue();
        assertTrue(capturedUpdate.getUpdateObject().containsKey("$set"));
        assertEquals(new BigDecimal("0"), capturedUpdate.getUpdateObject().get("$set", Document.class).get("amount"));
        assertFalse(capturedUpdate.getUpdateObject().get("$set", Document.class).getBoolean("isActive"));
        assertNotNull(capturedUpdate.getUpdateObject().get("$set", Document.class).get("cancellationDate"));
    }

    @Test
    void givenInvalidSubscription_whenCancelSubscription_thenThrowSubscriptionNotFoundException() {
        // Given
        String fundId = "1";
        User user = new User();
        user.setId("userId");

        Fund fund = new Fund(fundId, "Test Fund", new BigDecimal("100"), "RFT");

        when(userService.getUser()).thenReturn(user);
        when(fundRepository.findById(fundId)).thenReturn(Optional.of(fund));
        when(subscriptionRepository.findByUserIdAndFundIdAndIsActive(user.getId(), fundId, true))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(SubscriptionNotFoundException.class, () ->
                fundService.cancelSubscription(fundId));
    }

    @Test
    void givenExistingUser_whenGetTransactionHistory_thenReturnTransactions() {
        // Given
        User user = new User();
        user.setId("userId");

        List<Transaction> expectedTransactions = Arrays.asList(
                new Transaction(),
                new Transaction()
        );

        when(userService.getUser()).thenReturn(user);
        when(transactionService.getUserTransactions(user.getId())).thenReturn(expectedTransactions);

        // When
        List<Transaction> actualTransactions = fundService.getTransactionHistory();

        // Then
        assertEquals(expectedTransactions, actualTransactions);
        verify(transactionService).getUserTransactions(user.getId());
    }
}