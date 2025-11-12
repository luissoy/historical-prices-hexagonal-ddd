package com.luissoy.historicalprices.product.application;

import com.luissoy.historicalprices.product.application.dto.CreateProductCommand;
import com.luissoy.historicalprices.product.application.dto.ProductResult;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CreateProductUseCaseTest {

    private ProductRepository productRepository;
    private ProductMapper productMapper;
    private CreateProductUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productMapper = mock(ProductMapper.class);
        useCase = new CreateProductUseCase(productRepository, productMapper);
    }

    @Test
    void execute_should_create_save_and_return_mapped_product() {
        CreateProductCommand command = new CreateProductCommand("Cámara", "Cámara mirrorless 24MP");

        Product savedProduct = mock(Product.class);

        ProductResult expected = new ProductResult(
                1L,
                "Cámara",
                "Cámara mirrorless 24MP"
        );

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
        when(productMapper.toProductResult(savedProduct)).thenReturn(expected);

        ProductResult result = useCase.execute(command);
        assertThat(result).isSameAs(expected);
        verify(productRepository).save(any(Product.class));
        verify(productMapper).toProductResult(savedProduct);
        verifyNoMoreInteractions(productRepository, productMapper);
    }
}