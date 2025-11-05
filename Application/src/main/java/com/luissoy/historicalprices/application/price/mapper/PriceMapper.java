package com.luissoy.historicalprices.application.price.mapper;

import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;

public class PriceMapper {
    public PriceResult toDto(Price price) {
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
