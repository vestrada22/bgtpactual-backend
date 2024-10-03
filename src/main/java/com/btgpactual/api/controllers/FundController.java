package com.btgpactual.api.controllers;

import com.btgpactual.api.dto.FundCancelSubscriptionDto;
import com.btgpactual.api.dto.FundSubscriptionRequestDto;
import com.btgpactual.business.services.FundService;
import com.btgpactual.data.entities.Fund;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    public List<Fund> getAllFunds() {
        return fundService.findAllFunds();
    }

    @PostMapping("/{fundId}/subscribe")
    public Fund subscribeTo(@Valid @RequestBody FundSubscriptionRequestDto dto) {
        return fundService.subscribeTo(dto);
    }

    @PostMapping("/{fundId}/cancel")
    public Fund cancelSubscription(@Valid @RequestBody FundCancelSubscriptionDto dto) {
        return fundService.cancelSubscription(dto);
    }

}
