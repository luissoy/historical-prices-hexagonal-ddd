package com.luissoy.historicalprices.product.domain.valueobject;

import com.luissoy.historicalprices.product.domain.exception.InvalidProductDescriptionException;

public record ProductDescription(String value) {
    public ProductDescription {
        if (value == null || value.isBlank())
            throw new InvalidProductDescriptionException();
    }
}