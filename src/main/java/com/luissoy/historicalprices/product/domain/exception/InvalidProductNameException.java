package com.luissoy.historicalprices.product.domain.exception;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;

public class InvalidProductNameException extends ValidationException {
    public InvalidProductNameException() {
        super("Product name cannot be null or empty");
    }
}