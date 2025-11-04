package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.mapper.PriceMapper;
import com.luissoy.historicalprices.application.price.port.in.PriceUseCase;
import com.luissoy.historicalprices.application.price.port.out.LoadPricePort;
import com.luissoy.historicalprices.application.product.port.out.LoadProductPort;
import com.luissoy.historicalprices.application.price.port.out.SavePricePort;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResponse;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class PriceService implements PriceUseCase {

    private final LoadProductPort loadProduct;
    private final LoadPricePort loadPrice;
    private final SavePricePort savePrice;
    private final PriceMapper priceMapper;

    public PriceService(
            LoadProductPort loadProduct,
            LoadPricePort loadPrice,
            SavePricePort savePrice,
            PriceMapper priceMapper
    ) {
        this.loadProduct = loadProduct;
        this.loadPrice = loadPrice;
        this.savePrice = savePrice;
        this.priceMapper = priceMapper;
    }

    @Override
    public PriceResponse addPrice(Long productIdLong, PriceCommand command) {
        ProductId productId = new ProductId(productIdLong);

        Product product = loadProduct.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        Money money = new Money(command.value(), new Currency(command.currencyCode()));
        DateRange dateRange = new DateRange(command.initDate(), command.endDate());

        List<Price> existing = loadPrice.findByProductId(productId);

        Price newPrice = PriceFactory.createPrice(
                null,
                product.id(),
                money,
                dateRange,
                existing
        );

        Price saved = savePrice.save(newPrice);

        return priceMapper.toDto(saved);
    }

    @Override
    public PriceResponse getActivePrice(Long productIdLong, LocalDateTime applicationDate) {
        ProductId productId = new ProductId(productIdLong);

        Optional<Price> price = loadPrice.findByProductIdAndDate(productId, applicationDate);
        if (price.isEmpty()) {
            throw new PriceNotFoundException(productId);
        }

        return priceMapper.toDto(price.get());
    }

    @Override
    public PriceHistoryResponse getPriceHistory(Long productIdLong) {
        ProductId productId = new ProductId(productIdLong);

        List<Price> prices = loadPrice.findByProductId(productId);

        List<PriceResponse> dtos = prices.stream()
                .map(priceMapper::toDto)
                .collect(Collectors.toList());

        return new PriceHistoryResponse(productIdLong, dtos);
    }
}