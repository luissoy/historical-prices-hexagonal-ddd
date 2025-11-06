package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.mapper.PriceMapper;
import com.luissoy.historicalprices.application.price.port.in.PriceUseCase;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PriceService implements PriceUseCase {

    private final PriceRepository priceRepository;
    private final ProductRepository productRepository;
    private final PriceMapper priceMapper;

    public PriceService(
            PriceRepository priceRepository,
            ProductRepository productRepository,
            PriceMapper priceMapper
    ) {
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public PriceResult addPrice(Long productIdLong, PriceCommand command) {
        ProductId productId = new ProductId(productIdLong);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Money money = new Money(command.value(), new Currency(command.currencyCode()));
        DateRange dateRange = new DateRange(command.initDate(), command.endDate());

        List<Price> existing = priceRepository.findByProductId(productId);

        Price newPrice = PriceFactory.createPrice(
                null,
                product.id(),
                money,
                dateRange,
                existing
        );

        Price saved = priceRepository.save(newPrice);

        return priceMapper.toDto(saved);
    }

    @Override
    public PriceResult getActivePrice(Long productIdLong, LocalDate applicationDate) {
        ProductId productId = new ProductId(productIdLong);

        Optional<Price> price = priceRepository.findByProductIdAndDate(productId, applicationDate);
        if (price.isEmpty()) {
            throw new PriceNotFoundException(productId);
        }

        return priceMapper.toDto(price.get());
    }

    @Override
    public PriceHistoryResult getPriceHistory(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        List<Price> prices = priceRepository.findByProductId(productId);

        List<PriceResult> dtos = prices.stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());

        return priceMapper.toPriceHistoryDto(product, dtos);
    }
}