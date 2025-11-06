package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.valueobject.*;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PriceEntityMapper {

    public PriceEntity toEntity(Price price) {
        return new PriceEntity(
                price.id() != null ? price.id().getValue() : null,
                price.value().amount(),
                price.value().currency().code(),
                price.dateRange().start(),
                price.dateRange().end(),
                price.productId().getValue()
        );
    }

    public Price toDomain(PriceEntity entity) {
        return PriceFactory.createPrice(
                new PriceId(entity.getId()),
                new ProductId(entity.getProductId()),
                new Money(entity.getValue(), new Currency(entity.getCurrencyCode())),
                new DateRange(entity.getInitDate(), entity.getEndDate()),
                List.of()
        );
    }
}
