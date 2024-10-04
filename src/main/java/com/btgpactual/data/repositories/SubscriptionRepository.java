package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    Optional<Subscription> findByUserIdAndFundIdAndIsActive(String userId, String fundId, Boolean isActive);
}
