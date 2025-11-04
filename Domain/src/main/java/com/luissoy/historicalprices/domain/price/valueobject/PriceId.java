package com.luissoy.historicalprices.domain.price.valueobject;

import com.luissoy.historicalprices.domain.shared.valueobject.Identifier;

public final class PriceId extends Identifier<Long> {
    public PriceId(Long value) {
        super(value);
    }
}
