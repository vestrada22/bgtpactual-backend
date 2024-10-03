package com.btgpactual.api.dto;

import com.btgpactual.data.entities.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionRequestDto {
    private String userId;
    private String fundId;
    private BigDecimal amount;
    private Transaction.TransactionType type;
}
