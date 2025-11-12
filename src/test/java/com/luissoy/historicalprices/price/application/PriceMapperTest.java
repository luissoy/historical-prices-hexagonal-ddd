package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PriceMapperTest {

    @Test
    void toPriceResult_should_map_all_fields_correctly() {
        PriceId priceId = new PriceId(10L);
        ProductId productId = new ProductId(1L);
        Money money = new Money(new BigDecimal("19.99"), new Currency("EUR"));
        DateRange dateRange = new DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31));

        Price price = new Price(priceId, productId, money, dateRange);
        PriceMapper mapper = new PriceMapper();
        PriceResult result = mapper.toPriceResult(price);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.productId()).isEqualTo(1L);
        assertThat(result.value()).isEqualByComparingTo("19.99");
        assertThat(result.currency()).isEqualTo("EUR");
        assertThat(result.startDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.endDate()).isEqualTo(LocalDate.of(2024, 12, 31));
    }
}
