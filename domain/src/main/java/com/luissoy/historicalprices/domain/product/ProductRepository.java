package com.luissoy.historicalprices.domain.product;

import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(ProductId id);
    List<Product> findAll();
    Product save(Product product);
    void delete(ProductId id);
}