package com.luissoy.historicalprices.infrastructure.out.persistence.product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.*;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                new ProductId(entity.id()),
                new ProductName(entity.name()),
                new ProductDescription(entity.description())
        );
    }


    public Product resultSetToDomain(ResultSet rs) throws SQLException {
        return new Product(
                new ProductId(rs.getLong("id")),
                new ProductName(rs.getString("name")),
                new ProductDescription(rs.getString("description"))
        );
    }
}
