package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

import java.util.List;

public class GetProductPriceHistoryUseCase {

    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;

    public GetProductPriceHistoryUseCase(ProductRepository productRepository, PriceMapper priceMapper) {
        this.productRepository = productRepository;
        this.priceMapper = priceMapper;
    }

    public List<PriceResult> execute(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return product.prices().stream()
                .map(priceMapper::toPriceResult)
                .toList();
    }
}