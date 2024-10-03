package com.btgpactual.api.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FundCancelSubscriptionDto {
    @NotNull(message = "User ID cannot be null")
    @NotEmpty(message = "User ID cannot be empty")
    private String userId;

    @NotNull(message = "Fund ID cannot be null")
    @NotEmpty(message = "Fund ID cannot be empty")
    private String fundId;
}
