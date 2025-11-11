package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.ProductRequest;
import com.luissoy.historicalprices.api.model.ProductResponse;
import com.luissoy.historicalprices.application.product.dto.CreateProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;
import org.springframework.stereotype.Component;

@Component
public class ProductApiMapper {
    public CreateProductCommand toProductCommand(ProductRequest r) {
        return new CreateProductCommand(
                r.getName()
                , r.getDescription()
        );
    }

    public ProductResponse toProductResponse(ProductResult result) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(result.id());
        productResponse.setName(result.name());
        productResponse.setDescription(result.description());
        return productResponse;
    }
}
