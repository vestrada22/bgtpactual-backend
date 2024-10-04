package com.btgpactual.api.controllers;

import com.btgpactual.api.dto.FundDto;
import com.btgpactual.api.dto.FundSubscriptionRequestDto;
import com.btgpactual.api.dto.TransactionResponseDto;
import com.btgpactual.business.services.FundService;
import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.entities.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/funds")
@RequiredArgsConstructor
public class FundController {

    private final FundService fundService;

    @GetMapping
    public ResponseEntity<List<FundDto>> getAllFunds() {
        List<Fund> funds = fundService.findAllFunds();
        List<FundDto> fundsDtoList = funds.stream()
                .map(fund -> new FundDto(fund.getId(), fund.getName(), fund.getMinimumAmount(), fund.getCategory()))
                .toList();
        return ResponseEntity.ok(fundsDtoList);
    }

    @PostMapping("/{fundId}/subscribe")
    public ResponseEntity<TransactionResponseDto> subscribeTo(@PathVariable String fundId, @Valid @RequestBody FundSubscriptionRequestDto dto) {
        Transaction transaction = fundService.subscribeTo(fundId, dto.getAmount(), dto.getNotificationPreference());
        return ResponseEntity.ok(new TransactionResponseDto(transaction));
    }

    @PostMapping("/{fundId}/cancel")
    public ResponseEntity<TransactionResponseDto> cancelSubscription(@PathVariable String fundId) {
        Transaction transaction = fundService.cancelSubscription(fundId);
        return ResponseEntity.ok(new TransactionResponseDto(transaction));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionHistory() {
        List<Transaction> transactions = fundService.getTransactionHistory();
        List<TransactionResponseDto> transactionDTOs = transactions.stream()
                .map(TransactionResponseDto::new)
                .toList();
        return ResponseEntity.ok(transactionDTOs);
    }

}
