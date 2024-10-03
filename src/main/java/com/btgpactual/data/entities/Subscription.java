package com.btgpactual.data.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
public class Subscription {
    @Id
    private String id;
    private String userId;
    private String fundId;
    private BigDecimal amount;
    private LocalDateTime subscriptionDate;
}
