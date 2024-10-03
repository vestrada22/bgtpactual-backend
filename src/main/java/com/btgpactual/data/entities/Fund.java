package com.btgpactual.data.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "funds")
@Data
public class Fund {
    @Id
    private String id;
    private String name;
    private BigDecimal minimumAmount;
    private String category;
}
