package com.luissoy.historicalprices.domain.price.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

public record Currency(String code) {
    public Currency {
        if (code == null || !code.matches("[A-Z]{3}"))
            throw new ValidationException("Invalid currency code");
    }
}