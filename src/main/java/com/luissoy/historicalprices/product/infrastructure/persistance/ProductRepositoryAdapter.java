package com.luissoy.historicalprices.product.infrastructure.persistance;

import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductDataAccess productDataAccess;
    private final ProductAggregateAssembler productAssembler;

    public ProductRepositoryAdapter(
            ProductDataAccess productDataAccess,
            ProductAggregateAssembler productAssembler) {
        this.productDataAccess = productDataAccess;
        this.productAssembler = productAssembler;
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return productDataAccess.findById(id)
                .map(productAssembler::loadPrices);
    }

    @Override
    public List<Product> findAll() {
        return productDataAccess.findAll()
                .stream()
                .map(productAssembler::loadPrices)
                .toList();
    }

    @Override
    @Transactional
    public Product save(Product product) {
        Product saved = productDataAccess.saveBasicInfo(product);
        productAssembler.syncPrices(product.prices());
        return findById(saved.id()).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(ProductId id) {
        productDataAccess.delete(id);
    }
}