package com.luissoy.historicalprices.infrastructure.out.jpa.Product;

import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final JpaProductRepository jpa;
    private final ProductEntityMapper mapper;

    public ProductRepositoryAdapter(JpaProductRepository jpa, ProductEntityMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpa.findById(id.getValue()).map(mapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity saved = jpa.save(entity);

        return mapper.toDomain(saved);
    }

    @Override
    public void delete(ProductId id) {
        jpa.deleteById(id.getValue());
    }
}