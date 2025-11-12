package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;

public class ProductMapper {
    public ProductResult toProductResult(Product product) {
        return new ProductResult(
                product.id().getValue(),
                product.name().value(),
                product.description().value()
        );
    }
}
