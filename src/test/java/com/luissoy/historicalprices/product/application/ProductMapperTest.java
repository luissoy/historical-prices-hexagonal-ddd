package com.luissoy.historicalprices.product.application;

import com.luissoy.historicalprices.product.application.dto.ProductResult;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    @Test
    void toProductResult_should_map_all_fields() {
        Product product = new Product(
                new ProductId(1L),
                new ProductName("Cámara"),
                new ProductDescription("Cámara mirrorless 24MP")
        );

        ProductMapper mapper = new ProductMapper();

        ProductResult result = mapper.toProductResult(product);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Cámara");
        assertThat(result.description()).isEqualTo("Cámara mirrorless 24MP");
    }
}