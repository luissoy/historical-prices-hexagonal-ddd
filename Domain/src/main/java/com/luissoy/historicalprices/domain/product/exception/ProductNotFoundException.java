package com.luissoy.historicalprices.domain.product.exception;

import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(ProductId id) {
        super("Product with id " + id.toString() + " not found");
    }
}
