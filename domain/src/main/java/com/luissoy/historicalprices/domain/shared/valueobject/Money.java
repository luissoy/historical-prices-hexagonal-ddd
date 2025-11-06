package com.luissoy.historicalprices.domain.shared.valueobject;


import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

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