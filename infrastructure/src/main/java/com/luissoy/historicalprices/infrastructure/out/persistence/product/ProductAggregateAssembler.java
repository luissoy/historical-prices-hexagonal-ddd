package com.luissoy.historicalprices.infrastructure.out.persistence.product;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.product.Product;
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