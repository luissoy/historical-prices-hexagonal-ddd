package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.application.dto.PriceResult;

public class PriceMapper {
    public PriceResult toPriceResult(Price price) {
        return new PriceResult(
                price.id().getValue(),
                price.productId().getValue(),
                price.value().amount(),
                price.value().currency().code(),
                price.dateRange().start(),
                price.dateRange().end()
        );
    }
}
