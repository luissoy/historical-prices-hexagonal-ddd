package com.luissoy.historicalprices.price.domain;

import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

import java.time.LocalDate;

public class Price {
    private final PriceId id;
    private final ProductId productId;
    private final Money value;
    private final DateRange dateRange;

    public Price(PriceId id, ProductId productId, Money value, DateRange dateRange) {
        this.id = id;
        this.productId = productId;
        this.value = value;
        this.dateRange = dateRange;
    }

    public boolean isValidFor(LocalDate date) {
        return dateRange.includes(date);
    }

    public boolean overlaps(Price other) {
        return this.dateRange.overlaps(other.dateRange);
    }

    public PriceId id() { return id; }
    public ProductId productId() { return productId; }
    public Money value() { return value; }
    public DateRange dateRange() { return dateRange; }
}