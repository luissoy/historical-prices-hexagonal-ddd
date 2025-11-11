package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetPriceHistoryUseCaseTest {

    private ProductRepository productRepository;
    private PriceMapper priceMapper;
    private GetPriceHistoryUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        priceMapper = mock(PriceMapper.class);
        useCase = new GetPriceHistoryUseCase(productRepository, priceMapper);
    }

    @Test
    void execute_should_return_mapped_price_list_when_product_exists() {
        Long productIdLong = 1L;
        ProductId productId = new ProductId(productIdLong);

        Product product = mock(Product.class);
        Price price1 = mock(Price.class);
        Price price2 = mock(Price.class);
        List<Price> domainPrices = List.of(price1, price2);

        PriceResult result1 = new PriceResult(
                10L, productIdLong, new BigDecimal("9.99"), "EUR",
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 31)
        );
        PriceResult result2 = new PriceResult(
                11L, productIdLong, new BigDecimal("12.49"), "EUR",
                LocalDate.of(2024, 4, 1), LocalDate.of(2024, 12, 31)
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.prices()).thenReturn(domainPrices);
        when(priceMapper.toPriceResult(price1)).thenReturn(result1);
        when(priceMapper.toPriceResult(price2)).thenReturn(result2);

        List<PriceResult> results = useCase.execute(productIdLong);

        assertThat(results).containsExactly(result1, result2);
        verify(productRepository).findById(productId);
        verify(product).prices();
        verify(priceMapper).toPriceResult(price1);
        verify(priceMapper).toPriceResult(price2);
        verifyNoMoreInteractions(productRepository, priceMapper, product);
    }

    @Test
    void execute_should_throw_when_product_not_found() {
        Long productIdLong = 99L;
        ProductId productId = new ProductId(productIdLong);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(productIdLong))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository, priceMapper);
    }
}