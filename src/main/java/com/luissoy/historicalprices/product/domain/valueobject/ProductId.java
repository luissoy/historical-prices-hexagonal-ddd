package com.luissoy.historicalprices.product.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.valueobject.Identifier;

public final class ProductId extends Identifier<Long> {
    public ProductId(Long value) {
        super(value);
    }
}