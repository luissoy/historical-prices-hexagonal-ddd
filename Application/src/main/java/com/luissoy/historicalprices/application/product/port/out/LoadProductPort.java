package com.luissoy.historicalprices.application.product.port.out;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

import java.util.Optional;

public interface LoadProductPort {
    Optional<Product> findById(ProductId id);
}
