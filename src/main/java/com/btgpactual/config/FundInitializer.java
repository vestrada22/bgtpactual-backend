package com.btgpactual.config;

import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.repositories.FundRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class FundInitializer implements CommandLineRunner {

    private final FundRepository fundRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeFunds();
    }

    private void initializeFunds() {
        if (fundRepository.count() == 0) {
            List<Fund> initialFunds = Arrays.asList(
                    createFund("1", "FPV_BTG_PACTUAL_RECAUDADORA", new BigDecimal("75000"), "FPV"),
                    createFund("2", "FPV_BTG_PACTUAL_ECOPETROL", new BigDecimal("125000"), "FPV"),
                    createFund("3", "DEUDAPRIVADA", new BigDecimal("50000"), "FIC"),
                    createFund("4", "FDO-ACCIONES", new BigDecimal("250000"), "FIC"),
                    createFund("5", "FPV_BTG_PACTUAL_DINAMICA", new BigDecimal("100000"), "FPV")
            );

            fundRepository.saveAll(initialFunds);
            log.info("Funds initialized successfully!");
        } else {
            log.info("Funds already initialized!");
        }
    }

    private Fund createFund(String id, String name, BigDecimal minimumAmount, String category) {
        Fund fund = new Fund();
        fund.setId(id);
        fund.setName(name);
        fund.setMinimumAmount(minimumAmount);
        fund.setCategory(category);
        return fund;
    }
}
