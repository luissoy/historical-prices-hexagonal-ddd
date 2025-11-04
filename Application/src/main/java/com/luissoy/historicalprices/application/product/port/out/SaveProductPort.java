package com.luissoy.historicalprices.application.product.port.out;

import com.luissoy.historicalprices.domain.product.Product;

public interface SaveProductPort {
    Product save(Product product);
}
