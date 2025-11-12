package com.luissoy.historicalprices.product.application;

import com.luissoy.historicalprices.product.application.dto.ProductResult;
import com.luissoy.historicalprices.product.domain.Product;

public class ProductMapper {
    public ProductResult toProductResult(Product product) {
        return new ProductResult(
                product.id().getValue(),
                product.name().value(),
                product.description().value()
        );
    }
}
