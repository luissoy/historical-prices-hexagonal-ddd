package com.luissoy.historicalprices.infrastructure.out.persistence.product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductDataAccess {

    private static final String SELECT_BASE = "SELECT id, name, description FROM PRODUCTS";
    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";
    private static final String UPDATE_PRODUCT = "UPDATE PRODUCTS SET name = ?, description = ? WHERE id = ?";
    private static final String DELETE_PRODUCT = "DELETE FROM PRODUCTS WHERE id = ?";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert productInsert;
    private final ProductEntityMapper mapper;

    public ProductDataAccess(JdbcTemplate jdbc, ProductEntityMapper mapper, SimpleJdbcInsert productInsert) {
        this.jdbc = jdbc;
        this.productInsert = productInsert;
        this.mapper = mapper;
    }

    public Optional<Product> findById(ProductId id) {
        List<Product> result = jdbc.query(SELECT_BY_ID, (rs, rowNum) -> mapper.resultSetToDomain(rs), id.getValue());
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    public List<Product> findAll() {
        return jdbc.query(SELECT_BASE, (rs, rowNum) -> mapper.resultSetToDomain(rs));
    }

    public Product saveBasicInfo(Product product) {
        Long id = product.id() != null ? product.id().getValue() : null;

        if (id == null) {
            Map<String, Object> params = Map.of(
                    "name", product.name().value(),
                    "description", product.description().value()
            );
            id = productInsert.executeAndReturnKey(params).longValue();
        } else {
            jdbc.update(UPDATE_PRODUCT,
                    product.name().value(),
                    product.description().value(),
                    id);
        }

        return new Product(
                new ProductId(id),
                product.name(),
                product.description()
        );
    }

    public void delete(ProductId id) {
        jdbc.update(DELETE_PRODUCT, id.getValue());
    }
}