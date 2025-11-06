package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.CurrentPriceResponse;
import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceApiMapper {
    public PriceCommand toPriceCommand(PriceRequest priceRequest) {
        return new PriceCommand(
                priceRequest.getValue() == null ? null : BigDecimal.valueOf(priceRequest.getValue()),
                priceRequest.getCurrency(),
                priceRequest.getInitDate(),
                priceRequest.getEndDate() == null ? null : priceRequest.getEndDate()
        );
    }

    public PriceResponse toPriceResponse(PriceResult result) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setId(result.id());
        priceResponse.setProductId(result.productId());
        priceResponse.setValue(result.value().doubleValue());
        priceResponse.setCurrency(result.currency());
        priceResponse.setInitDate(result.startDate() != null ? result.startDate() : null);
        priceResponse.setEndDate(result.endDate() != null ? result.endDate() : null);
        return priceResponse;
    }

    public ProductWithPricesResponse toProductWithPricesResponse(PriceHistoryResult priceHistoryResult) {
        ProductWithPricesResponse response = new ProductWithPricesResponse();
        response.setId(priceHistoryResult.productId());
        response.setName(priceHistoryResult.productName());
        response.setDescription(priceHistoryResult.description());

        for (PriceResult priceResult : priceHistoryResult.prices()) {
            PriceResponse priceResponse = toPriceResponse(priceResult);
            response.getPrices().add(priceResponse);
        }

        return response;
    }

    public CurrentPriceResponse toCurrentPriceResponse(PriceResult result) {
        CurrentPriceResponse currentPriceResponse = new CurrentPriceResponse();
        currentPriceResponse.setCurrency(result.currency());
        currentPriceResponse.setValue(result.value().doubleValue());
        return currentPriceResponse;
    }
}
