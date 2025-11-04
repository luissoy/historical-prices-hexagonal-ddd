package com.luissoy.historicalprices.application.price.port.out;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LoadPricePort {

    List<Price> findByProductId(ProductId productId);

    Optional<Price> findByProductIdAndDate(ProductId productId, LocalDateTime applicationDate);
}