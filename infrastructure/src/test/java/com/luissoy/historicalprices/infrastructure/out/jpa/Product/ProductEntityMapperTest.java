package com.luissoy.historicalprices.infrastructure.out.jpa.Product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductEntityMapperTest {

    private ProductEntityMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductEntityMapper();
    }

    @Test
    void toEntity_shouldMapDomainToEntityCorrectly() {
        Product product = new Product(
                new ProductId(1L),
                new ProductName("Test Product"),
                new ProductDescription("A product used for testing")
        );

        ProductEntity entity = mapper.toEntity(product);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Test Product");
        assertThat(entity.getDescription()).isEqualTo("A product used for testing");
    }

    @Test
    void toDomain_shouldMapEntityToDomainCorrectly() {
        ProductEntity entity = new ProductEntity(5L, "Keyboard", "Mechanical keyboard");

        Product product = mapper.toDomain(entity);

        assertThat(product).isNotNull();
        assertThat(product.id().getValue()).isEqualTo(5L);
        assertThat(product.name().value()).isEqualTo("Keyboard");
        assertThat(product.description().value()).isEqualTo("Mechanical keyboard");
    }

    @Test
    void toDomainAndBack_shouldPreserveData() {
        Product original = new Product(
                new ProductId(2L),
                new ProductName("Monitor"),
                new ProductDescription("24-inch LED monitor")
        );

        ProductEntity entity = mapper.toEntity(original);
        Product result = mapper.toDomain(entity);

        assertThat(result.id().getValue()).isEqualTo(original.id().getValue());
        assertThat(result.name().value()).isEqualTo(original.name().value());
        assertThat(result.description().value()).isEqualTo(original.description().value());
    }
}
