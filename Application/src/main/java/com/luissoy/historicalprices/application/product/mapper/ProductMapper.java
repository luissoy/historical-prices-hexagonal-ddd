package com.luissoy.historicalprices.application.product.mapper;

import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;

public class ProductMapper {
    public ProductResult toDto(Product product) {
        return new ProductResult(
                product.id().getValue(),
                product.name().value(),
                product.description().value()
        );
    }
}
