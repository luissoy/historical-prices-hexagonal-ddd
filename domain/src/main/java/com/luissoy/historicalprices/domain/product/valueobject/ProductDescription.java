package com.luissoy.historicalprices.domain.product.valueobject;

import com.luissoy.historicalprices.domain.product.exception.InvalidProductDescriptionException;

public record ProductDescription(String value) {
    public ProductDescription {
        if (value == null || value.isBlank())
            throw new InvalidProductDescriptionException();
    }
}