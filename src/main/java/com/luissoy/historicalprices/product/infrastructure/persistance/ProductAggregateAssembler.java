package com.luissoy.historicalprices.product.infrastructure.persistance;

import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.PriceRepository;
import com.luissoy.historicalprices.product.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductAggregateAssembler {

    private final PriceRepository priceRepository;

    public ProductAggregateAssembler(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public Product loadPrices(Product product) {
        priceRepository.findByProductId(product.id())
                .forEach(product::addPrice);
        return product;
    }

    public void syncPrices(List<Price> prices) {
        for (Price price : prices) {
            if (price.id() == null) {
                priceRepository.save(price);
            }
        }
    }
}