package com.luissoy.historicalprices.product.domain.valueobject;

import com.luissoy.historicalprices.product.domain.exception.InvalidProductNameException;

public record ProductName(String value) {
    public ProductName {
        if (value == null || value.isBlank())
            throw new InvalidProductNameException();
    }
}
