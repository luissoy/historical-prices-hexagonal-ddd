package com.luissoy.historicalprices.product.domain;



import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.exception.OverlappingPriceException;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;
    private final List<Price> prices = new ArrayList<>();


    public Product(ProductId id, ProductName name, ProductDescription description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ProductId id() { return id; }
    public ProductName name() { return name; }
    public ProductDescription description() { return description; }
    public List<Price> prices() { return List.copyOf(prices); }

    public void addPrice(Price price) {
        boolean overlaps = prices.stream().anyMatch(existing -> existing.overlaps(price));
        if (overlaps) throw new OverlappingPriceException();
        prices.add(price);
    }

    public void addPrice(
            PriceId priceId,
            Money value,
            DateRange dateRange
    ) {
        Price newPrice = new Price(priceId, this.id, value, dateRange);
        addPrice(newPrice);
    }
}
