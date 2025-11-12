package com.luissoy.historicalprices.product.infrastructure.persistance;

import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.valueobject.ProductDescription;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductName;
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
