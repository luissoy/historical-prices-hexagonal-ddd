package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.application.dto.AddPriceCommand;
import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.Currency;
import com.luissoy.historicalprices.price.domain.valueobject.DateRange;
import com.luissoy.historicalprices.price.domain.valueobject.Money;
import com.luissoy.historicalprices.product.domain.Product;
import com.luissoy.historicalprices.product.domain.ProductRepository;
import com.luissoy.historicalprices.product.domain.exception.ProductNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

public class AddProductPriceUseCase {

    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;

    public AddProductPriceUseCase(ProductRepository productRepository, PriceMapper priceMapper) {
        this.productRepository = productRepository;
        this.priceMapper = priceMapper;
    }

    public PriceResult execute(AddPriceCommand command) {
        ProductId productId = new ProductId(command.productId());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Money money = new Money(command.value(), new Currency(command.currencyCode()));
        DateRange dateRange = new DateRange(command.initDate(), command.endDate());
        product.addPrice(null, money, dateRange);
        Product addedProduct = productRepository.save(product);
        Price addedPrice = addedProduct.prices().stream()
                .filter(p -> p.dateRange().equals(dateRange))
                .findFirst()
                .orElseThrow();

        return priceMapper.toPriceResult(addedPrice);
    }
}