package com.btgpactual.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FundDto {
    private String id;
    private String name;
    private BigDecimal minimumAmount;
    private String category;
}
