package com.luissoy.historicalprices.price.infrastructure.persistance;

import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

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
        Money money = new Money(
                entity.value(),
                new Currency(entity.currencyCode())
        );

        DateRange dateRange = new DateRange(
                entity.initDate(),
                entity.endDate()
        );

        return new Price(
                new PriceId(entity.id()),
                new ProductId(entity.productId()),
                money,
                dateRange
        );
    }

    public Price resultSetToDomain(ResultSet rs) throws SQLException {
        Money money = new Money(
                rs.getBigDecimal("price_value"),
                new Currency(rs.getString("currency_code"))
        );

        DateRange dateRange = new DateRange(
                rs.getObject("init_date", LocalDate.class),
                rs.getObject("end_date", LocalDate.class)
        );

        return new Price(
                new PriceId(rs.getLong("id")),
                new ProductId(rs.getLong("product_id")),
                money,
                dateRange
        );
    }
}
