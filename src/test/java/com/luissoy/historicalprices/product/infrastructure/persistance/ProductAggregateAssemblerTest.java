package com.luissoy.historicalprices.product.infrastructure.persistance;


import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.PriceRepository;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductAggregateAssemblerTest {

    private ProductAggregateAssembler assembler;
    private PriceRepository priceRepository;

    @BeforeEach
    void setUp() {
        priceRepository = mock(PriceRepository.class);
        assembler = new ProductAggregateAssembler(priceRepository);
    }

    @Test
    void loadPrices_shouldAddPricesToProduct() {
        Product product = new Product(
                new ProductId(1L),
                new ProductName("Test"),
                new ProductDescription("Desc")
        );

        Price price1 = new Price(
                new PriceId(1L),
                product.id(),
                new Money(BigDecimal.valueOf(100), new Currency("EUR")),
                new DateRange(LocalDate.of(2024,1,1), LocalDate.of(2024,6,30))
        );

        Price price2 = new Price(
                new PriceId(2L),
                product.id(),
                new Money(BigDecimal.valueOf(120), new Currency("EUR")),
                new DateRange(LocalDate.of(2024,7,1), null)
        );

        when(priceRepository.findByProductId(product.id()))
                .thenReturn(List.of(price1, price2));

        Product loaded = assembler.loadPrices(product);

        assertThat(loaded.prices()).hasSize(2);
    }

    @Test
    void syncPrices_shouldSaveOnlyNewPrices() {
        Product product = new Product(
                new ProductId(1L),
                new ProductName("Test"),
                new ProductDescription("Desc")
        );

        Price newPrice = new Price(
                null,
                product.id(),
                new Money(BigDecimal.valueOf(150), new Currency("EUR")),
                new DateRange(LocalDate.of(2024,10,1), null)
        );

        product.addPrice(newPrice.id(), newPrice.value(), newPrice.dateRange());

        assembler.syncPrices(product.prices());

        verify(priceRepository).save(any(Price.class));
    }
}
