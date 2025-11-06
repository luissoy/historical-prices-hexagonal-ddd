package com.luissoy.historicalprices.domain.price;

import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PriceTest {

    @Test
    @DisplayName("should return true when date is within the date range")
    void isValidFor_shouldReturnTrueWhenDateWithinRange() {
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );
        Price price = new Price(new PriceId(1L), new ProductId(1L),
                new Money(BigDecimal.TEN, new Currency("EUR")), range);

        boolean valid = price.isValidFor(LocalDate.of(2024, 6, 15));

        assertThat(valid).isTrue();
    }

    @Test
    @DisplayName("should return false when date is outside the date range")
    void isValidFor_shouldReturnFalseWhenDateOutsideRange() {
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31)
        );
        Price price = new Price(new PriceId(1L), new ProductId(1L),
                new Money(BigDecimal.ONE, new Currency("EUR")), range);

        boolean valid = price.isValidFor(LocalDate.of(2024, 2, 1));

        assertThat(valid).isFalse();
    }

    @Test
    @DisplayName("should return true when date ranges overlap")
    void overlaps_shouldReturnTrueWhenRangesOverlap() {
        Price price1 = new Price(new PriceId(1L), new ProductId(1L),
                new Money(BigDecimal.TEN, new Currency("EUR")),
                new DateRange(LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 6, 30)));

        Price price2 = new Price(new PriceId(2L), new ProductId(1L),
                new Money(BigDecimal.ONE, new Currency("EUR")),
                new DateRange(LocalDate.of(2024, 6, 1),
                        LocalDate.of(2024, 12, 31)));

        assertThat(price1.overlaps(price2)).isTrue();
    }

    @Test
    @DisplayName("should return false when date ranges do not overlap")
    void overlaps_shouldReturnFalseWhenRangesDoNotOverlap() {
        Price price1 = new Price(new PriceId(1L), new ProductId(1L),
                new Money(BigDecimal.TEN, new Currency("EUR")),
                new DateRange(LocalDate.of(2024, 1, 1),
                        LocalDate.of(2024, 3, 31)));

        Price price2 = new Price(new PriceId(2L), new ProductId(1L),
                new Money(BigDecimal.ONE, new Currency("EUR")),
                new DateRange(LocalDate.of(2024, 4, 1),
                        LocalDate.of(2024, 6, 30)));

        assertThat(price1.overlaps(price2)).isFalse();
    }

    @Test
    @DisplayName("should return correct id, productId, value and dateRange")
    void getters_shouldReturnCorrectValues() {
        PriceId priceId = new PriceId(10L);
        ProductId productId = new ProductId(100L);
        Money money = new Money(BigDecimal.valueOf(99.99), new Currency("USD"));
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        Price price = new Price(priceId, productId, money, range);

        assertThat(price.id()).isEqualTo(priceId);
        assertThat(price.productId()).isEqualTo(productId);
        assertThat(price.value()).isEqualTo(money);
        assertThat(price.dateRange()).isEqualTo(range);
    }
}
