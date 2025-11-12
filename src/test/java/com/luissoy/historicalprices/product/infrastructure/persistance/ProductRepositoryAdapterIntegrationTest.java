package com.luissoy.historicalprices.product.infrastructure.persistance;

import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductRepositoryAdapterIntegrationTest {

    @Autowired
    private ProductRepositoryAdapter productRepository;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void setup() {
        jdbc.execute("DELETE FROM prices");
        jdbc.execute("DELETE FROM products");

        jdbc.update("INSERT INTO products (id, name, description) VALUES (1, 'Test Product', 'Description')");
    }

    @Test
    void save_and_findById_shouldWork() {
        Product product = new Product(
                null,
                new ProductName("New Product"),
                new ProductDescription("Desc")
        );

        Product saved = productRepository.save(product);

        Optional<Product> found = productRepository.findById(saved.id());
        assertThat(found).isPresent();
        assertThat(found.get().name().value()).isEqualTo("New Product");
        assertThat(found.get().description().value()).isEqualTo("Desc");
    }

    @Test
    void findAll_shouldReturnAllProducts() {
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        assertThat(products.get(0).name().value()).isEqualTo("Test Product");
    }

    @Test
    void delete_shouldRemoveProduct() {
        ProductId id = productRepository.findAll().get(0).id();

        productRepository.delete(id);
        Optional<Product> product = productRepository.findById(id);
        assertThat(product).isEmpty();
    }
}
