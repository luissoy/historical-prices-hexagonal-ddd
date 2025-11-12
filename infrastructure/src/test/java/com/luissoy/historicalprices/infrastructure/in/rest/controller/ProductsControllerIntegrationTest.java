package com.luissoy.historicalprices.infrastructure.in.rest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateProduct() throws Exception {
        String productJson = """
            {
              "name": "Monitor",
              "description": "27-inch 4K"
            }
            """;

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Monitor"));
    }

    @Test
    void shouldAddPriceToProduct() throws Exception {
        String productJson = """
            {
              "name": "Keyboard",
              "description": "Wireless"
            }
            """;

        String response = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String productId = response.replaceAll(".*\"id\":(\\d+).*", "$1");

        String priceJson = """
            {
              "value": 99.99,
              "currency": "EUR",
              "initDate": "%s"
            }
            """.formatted(LocalDate.now().toString());

        mockMvc.perform(post("/api/products/" + productId + "/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(priceJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.value").value(99.99));
    }

    @Test
    void shouldReturnPriceHistory() throws Exception {
        mockMvc.perform(get("/api/products/1/prices"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetCurrentPrice() throws Exception {
        String applicationDate = "2024-06-15";

        mockMvc.perform(get("/api/products/{productId}/prices/current", 1)
                        .param("date", applicationDate.toString()))
                .andExpect(status().isOk());
    }
}
