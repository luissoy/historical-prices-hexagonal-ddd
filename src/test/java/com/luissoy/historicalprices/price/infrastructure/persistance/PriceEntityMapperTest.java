package com.luissoy.historicalprices.price.infrastructure.persistance;

import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class PriceEntityMapperTest {

    private PriceEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PriceEntityMapper();
    }

    @Test
    void toEntity_shouldMapDomainToEntityCorrectly() {
        Price price = new Price(
                new PriceId(1L),
                new ProductId(2L),
                new Money(BigDecimal.valueOf(99.99), new Currency("EUR")),
                new DateRange(LocalDate.of(2024, 1, 1), null)
        );

        PriceEntity entity = mapper.toEntity(price);

        assertThat(entity.id()).isEqualTo(1L);
        assertThat(entity.productId()).isEqualTo(2L);
        assertThat(entity.value()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
        assertThat(entity.currencyCode()).isEqualTo("EUR");
        assertThat(entity.initDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(entity.endDate()).isNull();
    }

    @Test
    void toDomain_shouldMapEntityToDomainCorrectly() {
        PriceEntity entity = new PriceEntity(
                3L,
                BigDecimal.valueOf(150.00),
                "USD",
                LocalDate.of(2023, 5, 1),
                LocalDate.of(2023, 12, 1),
                5L
        );

        Price price = mapper.toDomain(entity);

        assertThat(price.id().getValue()).isEqualTo(3L);
        assertThat(price.productId().getValue()).isEqualTo(5L);
        assertThat(price.value().amount()).isEqualByComparingTo(BigDecimal.valueOf(150.00));
        assertThat(price.value().currency().code()).isEqualTo("USD");
        assertThat(price.dateRange().start()).isEqualTo(LocalDate.of(2023, 5, 1));
        assertThat(price.dateRange().end()).isEqualTo(LocalDate.of(2023, 12, 1));
    }

    @Test
    void resultSetToDomain_shouldMapCorrectly() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);

        when(rs.getLong("id")).thenReturn(1L);
        when(rs.getLong("product_id")).thenReturn(2L);
        when(rs.getBigDecimal("price_value")).thenReturn(BigDecimal.valueOf(50.0));
        when(rs.getString("currency_code")).thenReturn("EUR");
        when(rs.getObject("init_date", LocalDate.class)).thenReturn(LocalDate.of(2024, 1, 1));
        when(rs.getObject("end_date", LocalDate.class)).thenReturn(LocalDate.of(2024, 6, 1));

        Price price = mapper.resultSetToDomain(rs);

        assertThat(price.id().getValue()).isEqualTo(1L);
        assertThat(price.productId().getValue()).isEqualTo(2L);
        assertThat(price.value().amount()).isEqualByComparingTo(BigDecimal.valueOf(50.0));
        assertThat(price.value().currency().code()).isEqualTo("EUR");
        assertThat(price.dateRange().start()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(price.dateRange().end()).isEqualTo(LocalDate.of(2024, 6, 1));
    }
}
