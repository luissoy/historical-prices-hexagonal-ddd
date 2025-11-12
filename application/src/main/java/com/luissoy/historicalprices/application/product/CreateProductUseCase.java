package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.dto.CreateProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;

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
