package com.luissoy.historicalprices.domain.product;

import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;

public class Product {
    private final ProductId id;
    private final ProductName name;
    private final ProductDescription description;

    public Product(ProductId id, ProductName name, ProductDescription description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ProductId id() { return id; }

    public ProductName name() { return name; }

    public ProductDescription description() { return description; }
}