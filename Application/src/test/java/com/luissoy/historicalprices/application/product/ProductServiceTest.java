package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResponse;
import com.luissoy.historicalprices.application.product.mapper.ProductMapper;
import com.luissoy.historicalprices.application.product.port.out.LoadProductPort;
import com.luissoy.historicalprices.application.product.port.out.SaveProductPort;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private SaveProductPort saveProductPort;
    private LoadProductPort loadProductPort;
    private ProductMapper productMapper;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        saveProductPort = mock(SaveProductPort.class);
        loadProductPort = mock(LoadProductPort.class);
        productMapper = mock(ProductMapper.class);

        productService = new ProductService(saveProductPort, loadProductPort, productMapper);
    }

    @Test
    @DisplayName("should create a new product successfully")
    void createProduct() {
        ProductCommand command = new ProductCommand("Coca-Cola", "Carbonated drink");

        Product unsaved = new Product(null, new ProductName("Coca-Cola"), new ProductDescription("Carbonated drink"));
        Product saved = new Product(new ProductId(1L), unsaved.name(), unsaved.description());

        when(saveProductPort.save(any(Product.class))).thenReturn(saved);
        when(productMapper.toDto(saved)).thenReturn(
                new ProductResponse(1L, "Coca-Cola", "Carbonated drink")
        );

        ProductResponse response = productService.createProduct(command);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Coca-Cola");
        assertThat(response.description()).isEqualTo("Carbonated drink");

        verify(saveProductPort).save(any(Product.class));
        verify(productMapper).toDto(saved);
    }

    @Test
    @DisplayName("should return product when found")
    void getProduct() {
        ProductId productId = new ProductId(1L);
        Product product = new Product(productId, new ProductName("Pepsi"), new ProductDescription("Cola"));

        when(loadProductPort.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(
                new ProductResponse(1L, "Pepsi", "Cola")
        );

        ProductResponse response = productService.getProduct(1L);

        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("Pepsi");
        assertThat(response.description()).isEqualTo("Cola");

        verify(loadProductPort).findById(productId);
        verify(productMapper).toDto(product);
    }

    @Test
    @DisplayName("should throw ProductNotFoundException when product does not exist")
    void getProduct_notFound() {
        ProductId productId = new ProductId(1L);
        when(loadProductPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProduct(1L));

        verify(loadProductPort).findById(productId);
        verifyNoInteractions(productMapper);
    }
}
