package com.luissoy.historicalprices.domain.product.exception;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

public class InvalidProductNameException extends ValidationException {
    public InvalidProductNameException() {
        super("Product name cannot be null or empty");
    }
}