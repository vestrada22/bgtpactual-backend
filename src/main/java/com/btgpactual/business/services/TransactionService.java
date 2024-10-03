package com.btgpactual.business.services;

import com.btgpactual.api.dto.TransactionRequestDto;
import com.btgpactual.data.entities.Transaction;
import com.btgpactual.data.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Transactional
    public Transaction recordTransaction(TransactionRequestDto dto) {
        Transaction transaction = new Transaction();
        transaction.setUserId(dto.getUserId());
        transaction.setFundId(dto.getFundId());
        transaction.setAmount(dto.getAmount());
        transaction.setType(dto.getType());
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getUserTransactions(String userId) {
        return transactionRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public Optional<Transaction> findLastSubscription(String userId, String fundId) {
        return transactionRepository.findFirstByUserIdAndFundIdAndTypeOrderByTimestampDesc(userId, fundId, Transaction.TransactionType.SUBSCRIPTION);
    }
}
