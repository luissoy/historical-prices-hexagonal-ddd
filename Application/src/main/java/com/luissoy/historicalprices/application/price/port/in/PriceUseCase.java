package com.luissoy.historicalprices.application.price.port.in;

import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResponse;

import java.time.LocalDateTime;

public interface PriceUseCase {
    PriceResponse addPrice(Long productId, PriceCommand command);

    PriceResponse getActivePrice(Long productId, LocalDateTime applicationDate);

    PriceHistoryResponse getPriceHistory(Long productId);
}
