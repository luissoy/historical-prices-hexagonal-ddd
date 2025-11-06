package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.mapper.ProductMapper;
import com.luissoy.historicalprices.application.product.port.in.ProductUseCase;
import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;

public final class ProductService implements ProductUseCase {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResult createProduct(ProductCommand command) {
        ProductName name = new ProductName(command.name());
        ProductDescription description = new ProductDescription(command.description());

        Product toSave = new Product(null, name, description);

        Product persisted = productRepository.save(toSave);

        return productMapper.toDto(persisted);
    }

    @Override
    public ProductResult getProduct(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        return productRepository.findById(productId)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}