package com.luissoy.historicalprices.product.domain;

import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.exception.OverlappingPriceException;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ProductTest {

    @Test
    @DisplayName("should return correct id when created")
    void id() {
        ProductId id = new ProductId(1L);
        Product product = new Product(id, new ProductName("Laptop"), new ProductDescription("High-end device"));

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.id().getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should return correct name when created")
    void name() {
        ProductName name = new ProductName("Monitor");
        Product product = new Product(new ProductId(2L), name, new ProductDescription("4K monitor"));

        assertThat(product.name()).isEqualTo(name);
        assertThat(product.name().value()).isEqualTo("Monitor");
    }

    @Test
    @DisplayName("should return correct description when created")
    void description() {
        ProductDescription description = new ProductDescription("Wireless mouse");
        Product product = new Product(new ProductId(3L), new ProductName("Mouse"), description);

        assertThat(product.description()).isEqualTo(description);
        assertThat(product.description().value()).isEqualTo("Wireless mouse");
    }

    @Test
    @DisplayName("should add price when not overlapping")
    void addPrice_whenNotOverlapping_addsPrice() {
        Product product = new Product(new ProductId(10L), new ProductName("Prod"), new ProductDescription("Desc"));
        Money money = new Money(new BigDecimal("10.00"), new Currency("EUR"));

        product.addPrice(new PriceId(1L), money, new DateRange(LocalDate.of(2024,1,1), LocalDate.of(2024,1,31)));
        product.addPrice(new PriceId(2L), money, new DateRange(LocalDate.of(2024,2,1), LocalDate.of(2024,2,29)));

        assertThat(product.prices()).hasSize(2);
        assertThat(product.prices()).extracting(p -> p.id().getValue()).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("should throw when adding overlapping price")
    void addPrice_whenOverlapping_throwsException() {
        Product product = new Product(new ProductId(11L), new ProductName("Prod"), new ProductDescription("Desc"));
        Money money = new Money(new BigDecimal("10.00"), new Currency("EUR"));

        product.addPrice(new PriceId(1L), money, new DateRange(LocalDate.of(2024,1,1), LocalDate.of(2024,1,31)));

        assertThatThrownBy(() ->
                product.addPrice(new PriceId(2L), money, new DateRange(LocalDate.of(2024,1,15), LocalDate.of(2024,2,15)))
        ).isInstanceOf(OverlappingPriceException.class);

        assertThat(product.prices()).hasSize(1);
    }

    @Test
    @DisplayName("should expose immutable copy of prices list")
    void prices_returnsImmutableCopy() {
        Product product = new Product(new ProductId(12L), new ProductName("Prod"), new ProductDescription("Desc"));
        Money money = new Money(new BigDecimal("5.00"), new Currency("EUR"));
        product.addPrice(new PriceId(1L), money, new DateRange(LocalDate.of(2024,3,1), LocalDate.of(2024,3,31)));

        List<Price> snapshot = product.prices();

        assertThat(snapshot).hasSize(1);
        assertThatThrownBy(() -> snapshot.add(snapshot.getFirst())).isInstanceOf(UnsupportedOperationException.class);

        assertThat(product.prices()).hasSize(1);
    }

    @Test
    @DisplayName("should build Price from components in addPrice overload")
    void addPrice_overload_buildsPriceWithGivenData() {
        ProductId productId = new ProductId(13L);
        Product product = new Product(productId, new ProductName("Prod"), new ProductDescription("Desc"));
        PriceId priceId = new PriceId(99L);
        Money money = new Money(new BigDecimal("7.50"), new Currency("EUR"));
        DateRange dateRange = new DateRange(LocalDate.of(2024,4,1), LocalDate.of(2024,4,30));

        product.addPrice(priceId, money, dateRange);

        assertThat(product.prices()).hasSize(1);
        Price stored = product.prices().getFirst();
        assertThat(stored.id()).isEqualTo(priceId);
        assertThat(stored.productId()).isEqualTo(productId);
        assertThat(stored.value()).isEqualTo(money);
        assertThat(stored.dateRange()).isEqualTo(dateRange);
    }
}