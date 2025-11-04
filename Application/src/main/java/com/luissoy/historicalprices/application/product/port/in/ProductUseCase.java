package com.luissoy.historicalprices.application.product.port.in;

import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResponse;

public interface ProductUseCase {
    ProductResponse createProduct(ProductCommand command);
    ProductResponse getProduct(Long productId); // convenience; apps can extend
}