package com.luissoy.historicalprices.infrastructure.in.rest.mapper;

import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceApiMapper {
    public PriceCommand toPriceCommand(PriceRequest priceRequest) {
        return new PriceCommand(
                priceRequest.getValue() == null ? null : BigDecimal.valueOf(priceRequest.getValue()),
                priceRequest.getCurrency(),
                priceRequest.getInitDate().toLocalDateTime(),
                priceRequest.getEndDate() == null ? null : priceRequest.getEndDate().toLocalDateTime()
        );
    }

    public PriceResponse toPriceResponse(PriceResult result) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setId(result.id());
        priceResponse.setProductId(result.productId());
        priceResponse.setValue(result.value().doubleValue());
        priceResponse.setCurrency(result.currency());
        priceResponse.setInitDate(result.startDate() != null ? result.startDate().atOffset(java.time.ZoneOffset.UTC) : null);
        priceResponse.setEndDate(result.endDate() != null ? result.endDate().atOffset(java.time.ZoneOffset.UTC) : null);
        return priceResponse;
    }

    public ProductWithPricesResponse toProductWithPricesResponse(PriceHistoryResponse priceHistoryResponse) {
        ProductWithPricesResponse response = new ProductWithPricesResponse();
        response.setId(priceHistoryResponse.productId());
        response.setName(priceHistoryResponse.productName());
        response.setDescription(priceHistoryResponse.description());

        for (PriceResult priceResult : priceHistoryResponse.prices()) {
            PriceResponse priceResponse = toPriceResponse(priceResult);
            response.getPrices().add(priceResponse);
        }

        return response;
    }
}
