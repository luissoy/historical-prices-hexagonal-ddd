package com.luissoy.historicalprices.application.price.port.in;

import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResult;

import java.time.LocalDateTime;

public interface PriceUseCase {
    PriceResult addPrice(Long productId, PriceCommand command);

    PriceResult getActivePrice(Long productId, LocalDateTime applicationDate);

    PriceHistoryResponse getPriceHistory(Long productId);
}
