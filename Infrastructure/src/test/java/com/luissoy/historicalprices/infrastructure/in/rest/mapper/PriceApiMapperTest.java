package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriceApiMapperTest {

    private PriceApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceApiMapper();
    }

    @Test
    void toPriceCommand_shouldMapCorrectly() {
        PriceRequest request = new PriceRequest();
        request.setValue(45.5);
        request.setCurrency("EUR");
        request.setInitDate(OffsetDateTime.of(2024, 5, 1, 12, 0, 0, 0, ZoneOffset.UTC));
        request.setEndDate(OffsetDateTime.of(2024, 6, 1, 12, 0, 0, 0, ZoneOffset.UTC));

        PriceCommand command = mapper.toPriceCommand(request);

        assertThat(command.value()).isEqualByComparingTo(BigDecimal.valueOf(45.5));
        assertThat(command.currencyCode()).isEqualTo("EUR");
        assertThat(command.initDate()).isEqualTo(LocalDateTime.of(2024, 5, 1, 12, 0));
        assertThat(command.endDate()).isEqualTo(LocalDateTime.of(2024, 6, 1, 12, 0));
    }

    @Test
    void toPriceResponse_shouldMapCorrectly() {
        PriceResult result = new PriceResult(
                10L,
                2L,
                BigDecimal.valueOf(12.5),
                "USD",
                LocalDateTime.of(2023, 1, 1, 0, 0),
                LocalDateTime.of(2023, 2, 1, 0, 0)
        );

        PriceResponse response = mapper.toPriceResponse(result);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getProductId()).isEqualTo(2L);
        assertThat(response.getValue()).isEqualTo(12.5);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getInitDate()).isEqualTo(result.startDate().atOffset(ZoneOffset.UTC));
        assertThat(response.getEndDate()).isEqualTo(result.endDate().atOffset(ZoneOffset.UTC));
    }

    @Test
    void toProductWithPricesResponse_shouldMapPriceHistoryCorrectly() {
        PriceResult price1 = new PriceResult(1L, 10L, BigDecimal.TEN, "EUR",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0));

        PriceResult price2 = new PriceResult(2L, 10L, BigDecimal.valueOf(20), "EUR",
                LocalDateTime.of(2024, 2, 1, 0, 0),
                LocalDateTime.of(2024, 3, 1, 0, 0));

        PriceHistoryResponse history = new PriceHistoryResponse(
                10L, "ProductX", "Test description", List.of(price1, price2)
        );

        ProductWithPricesResponse response = mapper.toProductWithPricesResponse(history);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getName()).isEqualTo("ProductX");
        assertThat(response.getDescription()).isEqualTo("Test description");
        assertThat(response.getPrices()).hasSize(2);
        assertThat(response.getPrices().get(0).getValue()).isEqualTo(10.0);
        assertThat(response.getPrices().get(1).getValue()).isEqualTo(20.0);
    }

    @Test
    void toPriceCommand_shouldHandleNullEndDate() {
        PriceRequest request = new PriceRequest();
        request.setValue(100.0);
        request.setCurrency("EUR");
        request.setInitDate(OffsetDateTime.of(2024, 5, 1, 0, 0, 0, 0, ZoneOffset.UTC));
        request.setEndDate(null);

        PriceCommand command = mapper.toPriceCommand(request);

        assertThat(command.endDate()).isNull();
    }

    @Test
    void toPriceResponse_shouldHandleNullDatesGracefully() {
        PriceResult result = new PriceResult(
                1L, 2L, BigDecimal.TEN, "EUR", null, null
        );

        PriceResponse response = mapper.toPriceResponse(result);

        assertThat(response.getInitDate()).isNull();
        assertThat(response.getEndDate()).isNull();
    }

}
