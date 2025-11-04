package com.luissoy.historicalprices.domain.product.exception;

import com.luissoy.historicalprices.domain.shared.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + " not found");
    }
}
