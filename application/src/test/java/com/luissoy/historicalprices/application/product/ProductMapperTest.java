package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
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