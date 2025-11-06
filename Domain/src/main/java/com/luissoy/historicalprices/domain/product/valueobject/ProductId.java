package com.luissoy.historicalprices.domain.product.valueobject;

import com.luissoy.historicalprices.domain.shared.valueobject.Identifier;

public final class ProductId extends Identifier<Long> {
    public ProductId(Long value) {
        super(value);
    }
}