package com.btgpactual.api.dto;

import com.btgpactual.data.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    private String id;
    private String fundId;
    private BigDecimal amount;
    private String type;
    private LocalDateTime timestamp;

    public TransactionResponseDto(Transaction transaction) {
        this.id = transaction.getId();
        this.fundId = transaction.getFundId();
        this.amount = transaction.getAmount();
        this.type = transaction.getType().toString();
        this.timestamp = transaction.getTimestamp();
    }
}
