package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetProductUseCaseTest {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private GetProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productMapper = mock(ProductMapper.class);
        useCase = new GetProductUseCase(productRepository, productMapper);
    }

    @Test
    void execute_should_return_mapped_product_when_found() {
        Long productIdLong = 1L;
        ProductId productId = new ProductId(productIdLong);
        Product product = mock(Product.class);
        ProductResult expected = new ProductResult(1L, "Cámara", "Cámara mirrorless 24MP");

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toProductResult(product)).thenReturn(expected);

        ProductResult result = useCase.execute(productIdLong);

        assertThat(result).isSameAs(expected);
        verify(productRepository).findById(productId);
        verify(productMapper).toProductResult(product);
        verifyNoMoreInteractions(productRepository, productMapper);
    }

    @Test
    void execute_should_throw_when_not_found() {
        Long productIdLong = 99L;
        ProductId productId = new ProductId(productIdLong);

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(productIdLong))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(productId);
        verifyNoMoreInteractions(productRepository, productMapper);
    }
}