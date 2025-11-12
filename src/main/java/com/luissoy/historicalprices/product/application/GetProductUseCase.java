package com.luissoy.historicalprices.product.application;

import com.luissoy.historicalprices.product.application.dto.ProductResult;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

public class GetProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public GetProductUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResult execute(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return productMapper.toProductResult(product);
    }
}