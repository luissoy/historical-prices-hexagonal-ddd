package com.luissoy.historicalprices.application.price.port.in;

import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;

import java.time.LocalDate;

public interface PriceUseCase {
    PriceResult addPrice(Long productId, PriceCommand command);

    PriceResult getActivePrice(Long productId, LocalDate applicationDate);

    PriceHistoryResult getPriceHistory(Long productId);
}
