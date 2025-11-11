package com.luissoy.historicalprices.domain.price;

import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.price.valueobject.DateRange;
import com.luissoy.historicalprices.domain.price.valueobject.Money;

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