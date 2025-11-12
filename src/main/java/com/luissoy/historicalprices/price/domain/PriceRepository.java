package com.luissoy.historicalprices.price.domain;

import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRepository {
    Optional<Price> findById(PriceId id);
    List<Price> findByProductId(ProductId productId);
    Optional<Price> findByProductIdAndDate(ProductId id, LocalDate date);
    Price save(Price price);
    void delete(PriceId id);
}
