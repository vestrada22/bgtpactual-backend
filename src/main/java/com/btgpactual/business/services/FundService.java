package com.btgpactual.business.services;

import com.btgpactual.api.dto.FundCancelSubscriptionDto;
import com.btgpactual.api.dto.FundSubscriptionRequestDto;
import com.btgpactual.api.dto.TransactionRequestDto;
import com.btgpactual.business.exceptions.*;
import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.entities.Subscription;
import com.btgpactual.data.entities.Transaction;
import com.btgpactual.data.entities.User;
import com.btgpactual.data.repositories.FundRepository;
import com.btgpactual.data.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService {
    private final FundRepository fundRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;
    private final SubscriptionRepository subscriptionRepository;

    public List<Fund> findAllFunds() {
        return fundRepository.findAll();
    }

    @Transactional
    public Fund subscribeTo(FundSubscriptionRequestDto dto) {
        User user = userService.getUser();
        Fund fund = fundRepository.findById(dto.getFundId())
                .orElseThrow(() -> new FundNotFoundException("Fund not found"));

        if (dto.getAmount().compareTo(fund.getMinimumAmount()) < 0) {
            throw new MinimumAmountNotMetException("The minimum amount for this fund is " + fund.getMinimumAmount());
        }

        if (user.getBalance().compareTo(dto.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(user.getId());
        transactionRequestDto.setFundId(dto.getFundId());
        transactionRequestDto.setAmount(dto.getAmount());
        transactionRequestDto.setType(Transaction.TransactionType.SUBSCRIPTION);

        userService.updateBalance(dto.getUserId(), dto.getAmount().negate());
        Transaction transaction = transactionService.recordTransaction(transactionRequestDto);

        String message = String.format("You have successfully subscribed to the fund %s with an amount of %s. Transaction ID: %s",
                fund.getName(), dto.getAmount(), transaction.getId());
        String subject = "Fund Subscription Confirmation";
        notificationService.sendNotification(user, subject, message);
        return fund;
    }

    @Transactional
    public Fund cancelSubscription(FundCancelSubscriptionDto dto) {
        User user = userService.getUser();
        Fund fund = fundRepository.findById(dto.getFundId())
                .orElseThrow(() -> new FundNotFoundException("Fund not found"));

        Subscription subscription = subscriptionRepository.findByUserIdAndFundId(dto.getUserId(), dto.getFundId())
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found"));

        BigDecimal amountToReturn = subscription.getAmount();

        TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
        transactionRequestDto.setUserId(user.getId());
        transactionRequestDto.setFundId(dto.getFundId());
        transactionRequestDto.setAmount(fund.getMinimumAmount());
        transactionRequestDto.setType(Transaction.TransactionType.CANCELLATION);

        userService.updateBalance(user.getId(), amountToReturn);
        Transaction cancellation = transactionService.recordTransaction(transactionRequestDto);

        String message = String.format("Your subscription to the fund %s has been cancelled. %s has been returned to your account. Transaction ID: %s",
                fund.getName(), amountToReturn, cancellation.getId());
        String subject = "Cancel Subscription Confirmation";
        notificationService.sendNotification(user, subject,message);

        return fund;
    }
}
