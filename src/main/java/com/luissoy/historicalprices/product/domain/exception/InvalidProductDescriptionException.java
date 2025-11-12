package com.luissoy.historicalprices.product.domain.exception;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;

public class InvalidProductDescriptionException extends ValidationException {
    public InvalidProductDescriptionException() {
        super("Product description cannot be null or empty");
    }
}
