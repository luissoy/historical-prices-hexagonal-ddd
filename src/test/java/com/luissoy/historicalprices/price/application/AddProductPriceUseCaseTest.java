package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.application.dto.AddPriceCommand;
import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AddProductPriceUseCaseTest {

    ProductRepository productRepository;
    PriceMapper priceMapper;
    AddProductPriceUseCase useCase;


    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        priceMapper = mock(PriceMapper.class);
        useCase = new AddProductPriceUseCase(productRepository, priceMapper);
    }


    @Test
    void execute_should_add_price_and_return_result_when_product_exists() {
        AddPriceCommand command = new AddPriceCommand(
                1L,
                new BigDecimal("100.00"),
                "EUR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        PriceResult expectedResult = new PriceResult(
                1L,
                1L,
                new BigDecimal("100.00"),
                "EUR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        Product product = mock(Product.class);
        Product savedProduct = mock(Product.class);
        Price lastPrice = mock(Price.class);

        DateRange dateRange = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        List<Price> prices = new LinkedList<>();
        prices.add(lastPrice);

        when(productRepository.findById(new ProductId(1L))).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(savedProduct.prices()).thenReturn(prices);

        when(lastPrice.dateRange()).thenReturn(dateRange);

        when(priceMapper.toPriceResult(lastPrice)).thenReturn(expectedResult);

        PriceResult result = useCase.execute(command);

        assertThat(result).isSameAs(expectedResult);
        verify(productRepository).findById(new ProductId(1L));
        verify(product).addPrice(isNull(), any(), any());
        verify(productRepository).save(product);
        verify(priceMapper).toPriceResult(lastPrice);
    }


    @Test
    void execute_should_throw_exception_when_product_not_found() {
        AddPriceCommand command = new AddPriceCommand(
                1L,
                new BigDecimal("50.00"),
                "USD",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30)
        );

        when(productRepository.findById(new ProductId(1L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(ProductNotFoundException.class);
        verify(productRepository).findById(new ProductId(1L));
        verifyNoMoreInteractions(productRepository, priceMapper);
    }
}