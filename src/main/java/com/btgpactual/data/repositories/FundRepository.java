package com.btgpactual.data.repositories;

import com.btgpactual.data.entities.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundRepository extends MongoRepository<Fund, String> {
}
