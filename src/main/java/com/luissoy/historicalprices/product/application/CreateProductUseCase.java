package com.luissoy.historicalprices.product.application;

import com.luissoy.historicalprices.product.application.dto.CreateProductCommand;
import com.luissoy.historicalprices.product.application.dto.ProductResult;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;

public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public CreateProductUseCase(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResult execute(CreateProductCommand command) {
        Product product = new Product(
                null,
                new ProductName(command.name()),
                new ProductDescription(command.description())
        );

        Product saved = productRepository.save(product);
        return productMapper.toProductResult(saved);
    }
}
