package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PriceEntityMapperTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceEntityMapper();
    }

    @Test
    void toEntity_shouldMapDomainToEntityCorrectly() {
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                new ProductId(2L),
                new Money(BigDecimal.valueOf(99.99), new Currency("EUR")),
                new DateRange(LocalDateTime.of(2024, 1, 1, 0, 0), null),
                List.of()
        );

        PriceEntity entity = mapper.toEntity(price);

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getProductId()).isEqualTo(2L);
        assertThat(entity.getValue()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
        assertThat(entity.getCurrencyCode()).isEqualTo("EUR");
        assertThat(entity.getInitDate()).isEqualTo(LocalDateTime.of(2024, 1, 1, 0, 0));
        assertThat(entity.getEndDate()).isNull();
    }

    @Test
    void toDomain_shouldMapEntityToDomainCorrectly() {
        PriceEntity entity = new PriceEntity(
                3L,
                BigDecimal.valueOf(150.00),
                "USD",
                LocalDateTime.of(2023, 5, 1, 10, 0),
                LocalDateTime.of(2023, 12, 1, 10, 0),
                5L
        );

        Price price = mapper.toDomain(entity);

        assertThat(price.id().getValue()).isEqualTo(3L);
        assertThat(price.productId().getValue()).isEqualTo(5L);
        assertThat(price.value().amount()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
        assertThat(price.value().currency().code()).isEqualTo("USD");
        assertThat(price.dateRange().start()).isEqualTo(LocalDateTime.of(2023, 5, 1, 10, 0));
        assertThat(price.dateRange().end()).isEqualTo(LocalDateTime.of(2023, 12, 1, 10, 0));
    }
}
