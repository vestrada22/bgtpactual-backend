package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Optional<Subscription> findByUserIdAndFundId(String userId, String fundId);
}
