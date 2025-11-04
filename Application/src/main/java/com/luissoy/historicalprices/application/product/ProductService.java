package com.luissoy.historicalprices.application.product;

import com.luissoy.historicalprices.application.product.mapper.ProductMapper;
import com.luissoy.historicalprices.application.product.port.in.ProductUseCase;
import com.luissoy.historicalprices.application.product.port.out.LoadProductPort;
import com.luissoy.historicalprices.application.product.port.out.SaveProductPort;
import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResponse;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;

public final class ProductService implements ProductUseCase {

    private final SaveProductPort saveProductPort;
    private final LoadProductPort loadProductPort;
    private final ProductMapper productMapper;

    public ProductService(
            SaveProductPort saveProductPort,
            LoadProductPort loadProductPort,
            ProductMapper productMapper
    ) {
        this.saveProductPort = saveProductPort;
        this.loadProductPort = loadProductPort;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponse createProduct(ProductCommand command) {
        ProductName name = new ProductName(command.name());
        ProductDescription description = new ProductDescription(command.description());

        Product toSave = new Product(null, name, description);

        Product persisted = saveProductPort.save(toSave);

        return productMapper.toDto(persisted);
    }

    @Override
    public ProductResponse getProduct(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        return loadProductPort.findById(productId)
                .map(productMapper::toDto)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}