package com.luissoy.historicalprices.price.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.valueobject.Identifier;

public final class PriceId extends Identifier<Long> {
    public PriceId(Long value) {
        super(value);
    }
}
