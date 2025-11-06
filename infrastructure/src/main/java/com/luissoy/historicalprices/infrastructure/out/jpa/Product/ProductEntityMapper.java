package com.luissoy.historicalprices.infrastructure.out.jpa.Product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.*;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id() != null ? product.id().getValue() : null,
                product.name().value(),
                product.description().value()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return new Product(
                new ProductId(entity.getId()),
                new ProductName(entity.getName()),
                new ProductDescription(entity.getDescription())
        );
    }
}
