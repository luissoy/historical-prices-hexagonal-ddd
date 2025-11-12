package com.luissoy.historicalprices.price.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;

public record Currency(String code) {
    public Currency {
        if (code == null || !code.matches("[A-Z]{3}"))
            throw new ValidationException("Invalid currency code");
    }
}