package com.luissoy.historicalprices.infrastructure.out.jpa.Product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ProductRepositoryAdapter.class, ProductEntityMapper.class})
class ProductRepositoryAdapterIntegrationTest {

    @Autowired
    private ProductRepositoryAdapter adapter;

    @Autowired
    private JpaProductRepository jpa;

    @Test
    void shouldSaveAndRetrieveProduct() {
        Product product = new Product(null, new ProductName("Keyboard"), new ProductDescription("Mechanical keyboard"));

        Product saved = adapter.save(product);
        assertThat(saved.id()).isNotNull();

        Optional<Product> found = adapter.findById(saved.id());
        assertThat(found).isPresent();
        assertThat(found.get().name().value()).isEqualTo("Keyboard");
    }

    @Test
    void shouldFindAllProducts() {
        List<Product> products = adapter.findAll();
        jpa.save(new ProductEntity(null, "Mouse", "Wireless"));
        jpa.save(new ProductEntity(null, "Monitor", "27 inch"));

        List<Product> productsResult = adapter.findAll();

        assertThat(productsResult).hasSize(products.size() + 2);
    }

    @Test
    void shouldDeleteProduct() {
        ProductEntity entity = jpa.save(new ProductEntity(null, "Laptop", "Gaming"));
        adapter.delete(new ProductId(entity.getId()));

        assertThat(jpa.findById(entity.getId())).isEmpty();
    }
}
