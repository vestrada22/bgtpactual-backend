package com.btgpactual.api.controllers;

import com.btgpactual.api.dto.FundSubscriptionRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.btgpactual.data.repositories.FundRepository;
import com.btgpactual.data.entities.Fund;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class FundControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FundRepository fundRepository;

    private String testFundId;

    @BeforeEach
    void setUp() {
        // Crear un fondo de prueba
        Fund testFund = new Fund();
        testFund.setName("Test Fund");
        testFund.setMinimumAmount(BigDecimal.valueOf(500));
        testFund.setCategory("Test Category");
        Fund savedFund = fundRepository.save(testFund);
        testFundId = savedFund.getId();
    }

    @Test
    void whenSubscribeToFund_thenStatus200AndTransactionResponse() throws Exception {
        FundSubscriptionRequestDto requestDto = new FundSubscriptionRequestDto(BigDecimal.valueOf(1000), "EMAIL");
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/funds/{fundId}/subscribe", testFundId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.fundId").value(testFundId))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.type").value("SUBSCRIPTION"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void whenGetTransactionHistory_thenStatus200AndTransactionList() throws Exception {
        // Primero, realizar una suscripción para asegurar que haya una transacción
        FundSubscriptionRequestDto requestDto = new FundSubscriptionRequestDto(BigDecimal.valueOf(1000), "EMAIL");
        String requestBody = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/funds/{fundId}/subscribe", testFundId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // Luego, obtener el historial de transacciones
        mockMvc.perform(MockMvcRequestBuilders.get("/api/funds/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].fundId").value(testFundId))
                .andExpect(jsonPath("$[0].amount").value(1000))
                .andExpect(jsonPath("$[0].type").value("SUBSCRIPTION"))
                .andExpect(jsonPath("$[0].timestamp").exists());
    }
}