package com.btgpactual.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {
    @Id
    private String id;
    private String userId;
    private String fundId;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime timestamp;

    public enum TransactionType {
        SUBSCRIPTION,
        CANCELLATION
    }
}
