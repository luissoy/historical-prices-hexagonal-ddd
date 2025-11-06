package com.luissoy.historicalprices.application.price.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceResult(
        Long id,
        Long productId,
        BigDecimal value,
        String currency,
        LocalDate startDate,
        LocalDate endDate
) { }
