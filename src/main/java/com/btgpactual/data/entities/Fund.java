package com.btgpactual.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "funds")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Fund {
    @Id
    private String id;
    private String name;
    private BigDecimal minimumAmount;
    private String category;
}
