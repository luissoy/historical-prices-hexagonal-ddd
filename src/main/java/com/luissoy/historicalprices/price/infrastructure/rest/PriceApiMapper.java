package com.luissoy.historicalprices.price.infrastructure.rest;

import com.luissoy.historicalprices.api.model.CurrentPriceResponse;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.price.application.dto.AddPriceCommand;
import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.product.application.dto.ProductResult;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class PriceApiMapper {
    public AddPriceCommand toPriceCommand(Long productId, PriceRequest priceRequest) {
        return new AddPriceCommand(
                productId,
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

    public ProductWithPricesResponse toProductWithPricesResponse(ProductResult productResult, List<PriceResult> priceResults) {
        ProductWithPricesResponse response = new ProductWithPricesResponse();
        response.setId(productResult.id());
        response.setName(productResult.name());
        response.setDescription(productResult.description());

        for (PriceResult priceResult : priceResults) {
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
