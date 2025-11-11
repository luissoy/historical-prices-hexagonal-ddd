package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

import java.util.List;

public class GetPriceHistoryUseCase {

    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;

    public GetPriceHistoryUseCase(ProductRepository productRepository, PriceMapper priceMapper) {
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