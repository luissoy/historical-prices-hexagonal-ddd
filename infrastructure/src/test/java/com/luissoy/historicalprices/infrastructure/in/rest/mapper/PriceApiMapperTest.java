package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
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
        request.setInitDate(LocalDate.of(2024, 5, 1));
        request.setEndDate(LocalDate.of(2024, 6, 1));

        PriceCommand command = mapper.toPriceCommand(request);

        assertThat(command.value()).isEqualByComparingTo(BigDecimal.valueOf(45.5));
        assertThat(command.currencyCode()).isEqualTo("EUR");
        assertThat(command.initDate()).isEqualTo(LocalDate.of(2024, 5, 1));
        assertThat(command.endDate()).isEqualTo(LocalDate.of(2024, 6, 1));
    }

    @Test
    void toPriceResponse_shouldMapCorrectly() {
        PriceResult result = new PriceResult(
                10L,
                2L,
                BigDecimal.valueOf(12.5),
                "USD",
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 2, 1)
        );

        PriceResponse response = mapper.toPriceResponse(result);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getProductId()).isEqualTo(2L);
        assertThat(response.getValue()).isEqualTo(12.5);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getInitDate()).isEqualTo(result.startDate());
        assertThat(response.getEndDate()).isEqualTo(result.endDate());
    }

    @Test
    void toProductWithPricesResponse_shouldMapPriceHistoryCorrectly() {
        PriceResult price1 = new PriceResult(1L, 10L, BigDecimal.TEN, "EUR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1));

        PriceResult price2 = new PriceResult(2L, 10L, BigDecimal.valueOf(20), "EUR",
                LocalDate.of(2024, 2, 1),
                LocalDate.of(2024, 3, 1));

        PriceHistoryResult history = new PriceHistoryResult(
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
        request.setInitDate(LocalDate.of(2024, 5, 1));
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
