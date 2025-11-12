package com.luissoy.historicalprices.price.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;

import java.math.BigDecimal;

public record Money(BigDecimal amount, Currency currency) {
    public Money {
        if (amount == null)
            throw new ValidationException("Amount cannot be null");
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new ValidationException("Amount must be non-negative");
    }

    public boolean hasSameCurrency(Money other) {
        return this.currency.equals(other.currency);
    }
}