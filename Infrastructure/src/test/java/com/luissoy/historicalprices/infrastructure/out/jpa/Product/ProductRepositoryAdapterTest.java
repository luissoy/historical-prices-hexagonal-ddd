package com.luissoy.historicalprices.infrastructure.out.jpa.Product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class ProductRepositoryAdapterTest {

    private JpaProductRepository jpa;
    private ProductEntityMapper mapper;
    private ProductRepositoryAdapter adapter;

    @BeforeEach
    void setup() {
        jpa = mock(JpaProductRepository.class);
        mapper = new ProductEntityMapper();
        adapter = new ProductRepositoryAdapter(jpa, mapper);
    }

    @Test
    void findById_shouldReturnDomainObject_whenEntityExists() {
        ProductEntity entity = new ProductEntity(1L, "Test", "Description");
        when(jpa.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Product> result = adapter.findById(new ProductId(1L));

        assertThat(result).isPresent();
        assertThat(result.get().name().value()).isEqualTo("Test");
        verify(jpa).findById(1L);
    }

    @Test
    void findAll_shouldReturnListOfProducts() {
        List<ProductEntity> entities = List.of(
                new ProductEntity(1L, "Prod1", "Desc1"),
                new ProductEntity(2L, "Prod2", "Desc2")
        );
        when(jpa.findAll()).thenReturn(entities);

        List<Product> result = adapter.findAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).name().value()).isEqualTo("Prod1");
        verify(jpa).findAll();
    }

    @Test
    void save_shouldCallJpaAndReturnMappedProduct() {
        Product product = new Product(new ProductId(1L), new ProductName("Test"), new ProductDescription("Desc"));
        ProductEntity entity = mapper.toEntity(product);
        when(jpa.save(any(ProductEntity.class))).thenReturn(entity);

        Product saved = adapter.save(product);

        assertThat(saved.name().value()).isEqualTo("Test");
        verify(jpa).save(any(ProductEntity.class));
    }

    @Test
    void delete_shouldCallJpaDelete() {
        adapter.delete(new ProductId(1L));
        verify(jpa).deleteById(1L);
    }
}
