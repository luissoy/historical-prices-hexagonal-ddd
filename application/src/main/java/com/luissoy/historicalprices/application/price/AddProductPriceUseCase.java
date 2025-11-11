package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.PriceMapper;
import com.luissoy.historicalprices.application.price.dto.AddPriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.valueobject.Currency;
import com.luissoy.historicalprices.domain.price.valueobject.DateRange;
import com.luissoy.historicalprices.domain.price.valueobject.Money;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.Product;

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