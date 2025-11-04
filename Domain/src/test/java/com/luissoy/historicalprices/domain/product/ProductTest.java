package com.luissoy.historicalprices.domain.product;

import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    @DisplayName("should return correct id when created")
    void id() {
        ProductId id = new ProductId(1L);
        Product product = new Product(id, new ProductName("Laptop"), new ProductDescription("High-end device"));

        assertThat(product.id()).isEqualTo(id);
        assertThat(product.id().getValue()).isEqualTo(1L);
    }

    @Test
    @DisplayName("should return correct name when created")
    void name() {
        ProductName name = new ProductName("Monitor");
        Product product = new Product(new ProductId(2L), name, new ProductDescription("4K monitor"));

        assertThat(product.name()).isEqualTo(name);
        assertThat(product.name().value()).isEqualTo("Monitor");
    }

    @Test
    @DisplayName("should return correct description when created")
    void description() {
        ProductDescription description = new ProductDescription("Wireless mouse");
        Product product = new Product(new ProductId(3L), new ProductName("Mouse"), description);

        assertThat(product.description()).isEqualTo(description);
        assertThat(product.description().value()).isEqualTo("Wireless mouse");
    }
}
