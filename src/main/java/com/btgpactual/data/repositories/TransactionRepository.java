package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserIdOrderByTimestampDesc(String userId);
    Optional<Transaction> findFirstByUserIdAndFundIdAndTypeOrderByTimestampDesc(String userId, String fundId, Transaction.TransactionType type);
}
