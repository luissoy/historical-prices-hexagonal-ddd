package com.luissoy.historicalprices.domain.price;

import com.luissoy.historicalprices.domain.price.exception.OverlappingPriceException;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;

import java.util.List;

public class PriceFactory {
    public static Price createPrice(
            PriceId id,
            ProductId productId,
            Money value,
            DateRange dateRange,
            List<Price> existingPricesForProduct
    ) {
        boolean overlaps = existingPricesForProduct.stream()
                .anyMatch(existing -> existing.overlaps(new Price(id, productId, value, dateRange)));

        if (overlaps)
            throw new OverlappingPriceException();

        return new Price(id, productId, value, dateRange);
    }
}