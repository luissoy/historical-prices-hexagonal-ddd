package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.ProductRequest;
import com.luissoy.historicalprices.api.model.ProductResponse;
import com.luissoy.historicalprices.application.product.dto.CreateProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductApiMapperTest {

    private ProductApiMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductApiMapper();
    }

    @Test
    void toProductCommand_shouldMapCorrectly() {
        ProductRequest request = new ProductRequest();
        request.setName("Keyboard");
        request.setDescription("Mechanical keyboard");

        CreateProductCommand command = mapper.toProductCommand(request);

        assertThat(command.name()).isEqualTo("Keyboard");
        assertThat(command.description()).isEqualTo("Mechanical keyboard");
    }

    @Test
    void toProductResponse_shouldMapCorrectly() {
        ProductResult result = new ProductResult(1L, "Mouse", "Wireless mouse");

        ProductResponse response = mapper.toProductResponse(result);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Mouse");
        assertThat(response.getDescription()).isEqualTo("Wireless mouse");
    }
}
