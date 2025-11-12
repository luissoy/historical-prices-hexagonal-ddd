package com.luissoy.historicalprices.price.application;

import com.luissoy.historicalprices.price.application.dto.GetActivePriceCommand;
import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.price.domain.PriceRepository;
import com.luissoy.historicalprices.price.domain.exception.PriceNotFoundException;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

public class GetProductActivePriceUseCase {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public GetProductActivePriceUseCase(PriceRepository priceRepository, PriceMapper priceMapper) {
        this.priceRepository = priceRepository;
        this.priceMapper = priceMapper;
    }

    public PriceResult execute(GetActivePriceCommand command) {
        ProductId productId = new ProductId(command.productId());

        return priceRepository.findByProductIdAndDate(productId, command.applicationDate())
                .map(priceMapper::toPriceResult)
                .orElseThrow(() -> new PriceNotFoundException(productId));
    }
}