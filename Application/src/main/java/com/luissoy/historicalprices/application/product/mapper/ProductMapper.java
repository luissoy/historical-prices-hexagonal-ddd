package com.luissoy.historicalprices.application.product.mapper;

import com.luissoy.historicalprices.application.product.dto.ProductResponse;
import com.luissoy.historicalprices.domain.product.Product;

public interface ProductMapper {
    ProductResponse toDto(Product product);
}
