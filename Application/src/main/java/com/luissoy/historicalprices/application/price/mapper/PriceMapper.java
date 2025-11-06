package com.luissoy.historicalprices.application.price.mapper;

import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.product.Product;

import java.util.List;

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

    public PriceHistoryResult toPriceHistoryDto(Product product, List<PriceResult> dtos) {
        return new PriceHistoryResult(
                product.id().getValue(),
                product.name().value(),
                product.description().value(),
                dtos
        );
    }
}
