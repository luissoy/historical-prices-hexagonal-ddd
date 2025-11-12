package com.luissoy.historicalprices.product.domain.exception;

import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.shared.domain.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(ProductId id) {
        super("Product with id " + id.toString() + " not found");
    }
}
