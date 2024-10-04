package com.btgpactual.business.services;

import com.btgpactual.api.dto.TransactionRequestDto;
import com.btgpactual.business.exceptions.*;
import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.entities.Subscription;
import com.btgpactual.data.entities.Transaction;
import com.btgpactual.data.entities.User;
import com.btgpactual.data.repositories.FundRepository;
import com.btgpactual.data.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService {
    private final FundRepository fundRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;
    private final MongoTemplate mongoTemplate;

    public List<Fund> findAllFunds() {
        return fundRepository.findAll();
    }

    @Transactional
    public Transaction subscribeTo(String fundId, BigDecimal amount, String notificationPreference) {
        User user = userService.getUser();
        user.setNotificationPreference(notificationPreference.equalsIgnoreCase("email") ? User.NotificationType.EMAIL : User.NotificationType.SMS);
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new FundNotFoundException("Fund not found"));

        if (amount.compareTo(fund.getMinimumAmount()) < 0) {
            throw new MinimumAmountNotMetException("The minimum amount for this fund is " + fund.getMinimumAmount());
        }

        if (user.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(user.getId());
        transactionRequestDto.setFundId(fundId);
        transactionRequestDto.setAmount(amount);
        transactionRequestDto.setType(Transaction.TransactionType.SUBSCRIPTION);

        userService.updateBalance(user.getId(), amount.negate());
        Transaction transaction = transactionService.recordTransaction(transactionRequestDto);

        Subscription subscription = new Subscription();
        subscription.setUserId(user.getId());
        subscription.setFundId(fundId);
        subscription.setAmount(amount);
        subscription.setIsActive(true);
        subscription.setSubscriptionDate(LocalDateTime.now());
        subscriptionRepository.save(subscription);

        String message = String.format("You have successfully subscribed to the fund %s with an amount of %s. Transaction ID: %s",
                fund.getName(), amount, transaction.getId());
        String subject = "Fund Subscription Confirmation";
        notificationService.sendNotification(user, subject, message);
        return transaction;
    }

    @Transactional
    public Transaction cancelSubscription(String fundId) {
        User user = userService.getUser();
        Fund fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new FundNotFoundException("Fund not found"));

        Subscription subscription = subscriptionRepository.findByUserIdAndFundIdAndIsActive(user.getId(), fundId, true)
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        BigDecimal amountToReturn = subscription.getAmount();

        Query query = new Query(Criteria.where("id").is(subscription.getId()));
        Update subscriptionUpdate = new Update();

        subscriptionUpdate.set("amount", new BigDecimal("0"));
        subscriptionUpdate.set("isActive", false);
        subscriptionUpdate.set("cancellationDate", LocalDateTime.now());

        mongoTemplate.updateFirst(query, subscriptionUpdate, Subscription.class);

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(user.getId());
        transactionRequestDto.setFundId(fundId);
        transactionRequestDto.setAmount(fund.getMinimumAmount());
        transactionRequestDto.setType(Transaction.TransactionType.CANCELLATION);

        userService.updateBalance(user.getId(), amountToReturn);
        Transaction transaction = transactionService.recordTransaction(transactionRequestDto);

        String message = String.format("Your subscription to the fund %s has been cancelled. %s has been returned to your account. Transaction ID: %s",
                fund.getName(), amountToReturn, transaction.getId());
        String subject = "Cancel Subscription Confirmation";
        notificationService.sendNotification(user, subject,message);

        return transaction;
    }

    public List<Transaction> getTransactionHistory() {
        User user = userService.getUser();
        return transactionService.getUserTransactions(user.getId());
    }
}
