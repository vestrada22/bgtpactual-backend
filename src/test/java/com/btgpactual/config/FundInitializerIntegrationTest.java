package com.btgpactual.config;

import com.btgpactual.data.entities.Fund;
import com.btgpactual.data.repositories.FundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class FundInitializerIntegrationTest {

    @Autowired
    private FundInitializer fundInitializer;

    @SpyBean
    private FundRepository fundRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {
        mongoTemplate.dropCollection(Fund.class);
    }

    @Test
    void givenEmptyDatabase_whenRunInitializer_thenInitializeFunds() throws Exception {
        // Given
        when(fundRepository.count()).thenReturn(0L);

        // When
        fundInitializer.run();

        // Then
        verify(fundRepository, times(2)).count();
        verify(fundRepository, times(1)).saveAll(anyList());

        List<Fund> savedFunds = fundRepository.findAll();
        assertEquals(5, savedFunds.size());

        Fund fpvRecaudadora = savedFunds.stream()
                .filter(f -> f.getName().equals("FPV_BTG_PACTUAL_RECAUDADORA"))
                .findFirst()
                .orElseThrow();
        assertEquals("1", fpvRecaudadora.getId());
        assertEquals(new BigDecimal("75000"), fpvRecaudadora.getMinimumAmount());
        assertEquals("FPV", fpvRecaudadora.getCategory());

        // Verificar los otros fondos de manera similar...
    }

    @Test
    void givenExistingFunds_whenRunInitializer_thenDoNotInitializeFunds() throws Exception {
        // Given
        when(fundRepository.count()).thenReturn(5L);

        // When
        fundInitializer.run();

        // Then
        verify(fundRepository, times(1)).count();
        verify(fundRepository, never()).saveAll(anyList());

        List<Fund> savedFunds = fundRepository.findAll();
        assertTrue(savedFunds.isEmpty());
    }

    @Test
    void givenPartiallyPopulatedDatabase_whenRunInitializer_thenDoNotInitializeFunds() throws Exception {
        // Given
        Fund existingFund = new Fund();
        existingFund.setId("1");
        existingFund.setName("Existing Fund");
        existingFund.setMinimumAmount(new BigDecimal("100000"));
        existingFund.setCategory("FIC");
        fundRepository.save(existingFund);

        // When
        fundInitializer.run();

        // Then
        verify(fundRepository, times(1)).count();
        verify(fundRepository, never()).saveAll(anyList());

        List<Fund> savedFunds = fundRepository.findAll();
        assertEquals(1, savedFunds.size());
        assertEquals("Existing Fund", savedFunds.get(0).getName());
    }
}