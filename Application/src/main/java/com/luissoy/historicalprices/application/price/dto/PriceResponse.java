package com.luissoy.historicalprices.application.price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        Long priceId,
        Long productId,
        BigDecimal value,
        String currency,
        LocalDateTime startDate,
        LocalDateTime endDate
) { }
