package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.dto.GetActivePriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;

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