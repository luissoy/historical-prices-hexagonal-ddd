package com.luissoy.historicalprices.domain.product.valueobject;

import com.luissoy.historicalprices.domain.product.exception.InvalidProductNameException;

public record ProductName(String value) {
    public ProductName {
        if (value == null || value.isBlank())
            throw new InvalidProductNameException();
    }
}
