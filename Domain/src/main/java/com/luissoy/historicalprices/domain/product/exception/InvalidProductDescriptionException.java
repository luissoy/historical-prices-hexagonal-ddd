package com.luissoy.historicalprices.domain.product.exception;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

public class InvalidProductDescriptionException extends ValidationException {
    public InvalidProductDescriptionException() {
        super("Product description cannot be null or empty");
    }
}
