package com.luissoy.historicalprices.domain.price;

import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findById(PriceId id);
    List<Price> findByProductId(ProductId productId);
    Optional<Price> findByProductIdAndDate(ProductId id, LocalDateTime date);
    void save(Price price);
    void delete(PriceId id);
}
