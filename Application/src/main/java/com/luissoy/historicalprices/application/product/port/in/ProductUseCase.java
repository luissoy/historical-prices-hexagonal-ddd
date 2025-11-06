package com.luissoy.historicalprices.application.product.port.in;

import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;

public interface ProductUseCase {
    ProductResult createProduct(ProductCommand command);
    ProductResult getProduct(Long productId);
}