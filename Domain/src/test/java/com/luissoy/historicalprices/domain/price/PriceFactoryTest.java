package com.luissoy.historicalprices.domain.price;

import com.luissoy.historicalprices.domain.price.exception.OverlappingPriceException;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PriceFactoryTest {

    @Test
    @DisplayName("should create price successfully when no overlap exists")
    void createPrice_ShouldCreateSuccessfully_WhenNoOverlapExists() {
        PriceId id = new PriceId(1L);
        ProductId productId = new ProductId(100L);
        Money value = new Money(BigDecimal.valueOf(10.99), new Currency("EUR"));
        DateRange dateRange = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 2, 1, 0, 0)
        );

        List<Price> existingPrices = List.of();

        Price price = PriceFactory.createPrice(id, productId, value, dateRange, existingPrices);

        assertNotNull(price);
        assertEquals(id, price.id());
        assertEquals(productId, price.productId());
        assertEquals(value, price.value());
        assertEquals(dateRange, price.dateRange());
    }

    @Test
    @DisplayName("should throw OverlappingPriceException when overlap exists")
    void createPrice_ShouldThrowException_WhenOverlapExists() {
        ProductId productId = new ProductId(200L);
        Money value = new Money(BigDecimal.valueOf(9.99), new Currency("USD"));

        Price existing = new Price(
                new PriceId(1L),
                productId,
                value,
                new DateRange(
                        LocalDateTime.of(2024, 1, 1, 0, 0),
                        LocalDateTime.of(2024, 2, 1, 0, 0)
                )
        );

        DateRange overlappingRange = new DateRange(
                LocalDateTime.of(2024, 1, 15, 0, 0),
                LocalDateTime.of(2024, 3, 1, 0, 0)
        );

        assertThrows(
                OverlappingPriceException.class,
                () -> PriceFactory.createPrice(
                        new PriceId(2L),
                        productId,
                        value,
                        overlappingRange,
                        List.of(existing)
                )
        );
    }

    @Test
    @DisplayName("should create price successfully when date ranges touch but do not overlap")
    void createPrice_ShouldSucceed_WhenDateRangesTouchButDoNotOverlap() {
        ProductId productId = new ProductId(300L);
        Money value = new Money(BigDecimal.valueOf(20.0), new Currency("EUR"));

        Price existing = new Price(
                new PriceId(1L),
                productId,
                value,
                new DateRange(
                        LocalDateTime.of(2024, 1, 1, 0, 0),
                        LocalDateTime.of(2024, 2, 1, 0, 0)
                )
        );

        DateRange newRange = new DateRange(
                LocalDateTime.of(2024, 2, 1, 0, 1),
                LocalDateTime.of(2024, 3, 1, 0, 0)
        );

        Price newPrice = PriceFactory.createPrice(
                new PriceId(2L),
                productId,
                value,
                newRange,
                List.of(existing)
        );

        assertNotNull(newPrice);
        assertEquals(productId, newPrice.productId());
        assertEquals(newRange, newPrice.dateRange());
    }
}
