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
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class PriceRepositoryAdapterTest {

    private JpaPriceRepository jpa;
    private PriceEntityMapper mapper;
    private PriceRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        jpa = mock(JpaPriceRepository.class);
        mapper = new PriceEntityMapper();
        adapter = new PriceRepositoryAdapter(jpa, mapper);
    }

    @Test
    void findById_shouldReturnPriceDomain() {
        PriceEntity entity = new PriceEntity(1L, BigDecimal.TEN, "EUR",
                LocalDateTime.now().minusDays(1), LocalDateTime.now(), 10L);
        when(jpa.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Price> result = adapter.findById(new PriceId(1L));

        assertThat(result).isPresent();
        assertThat(result.get().value().amount()).isEqualTo(BigDecimal.TEN);
        verify(jpa).findById(1L);
    }

    @Test
    void findByProductId_shouldReturnList() {
        List<PriceEntity> entities = List.of(
                new PriceEntity(1L, BigDecimal.TEN, "EUR", LocalDateTime.now(), null, 10L)
        );
        when(jpa.findByProductId(10L)).thenReturn(entities);

        List<Price> result = adapter.findByProductId(new ProductId(10L));

        assertThat(result).hasSize(1);
        verify(jpa).findByProductId(10L);
    }

    @Test
    void save_shouldPersistEntityAndReturnDomain() {
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                new ProductId(10L),
                new Money(BigDecimal.TEN, new Currency("EUR")),
                new DateRange(LocalDateTime.now(), null),
                List.of()
        );
        PriceEntity entity = mapper.toEntity(price);
        when(jpa.save(any(PriceEntity.class))).thenReturn(entity);

        Price saved = adapter.save(price);

        assertThat(saved.value().amount()).isEqualTo(BigDecimal.TEN);
        verify(jpa).save(any(PriceEntity.class));
    }

    @Test
    void delete_shouldInvokeJpaDelete() {
        adapter.delete(new PriceId(1L));
        verify(jpa).deleteById(1L);
    }
}
