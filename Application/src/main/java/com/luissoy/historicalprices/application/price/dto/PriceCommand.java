package com.luissoy.historicalprices.application.price.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceCommand(
        BigDecimal value,
        String currencyCode,
        LocalDateTime initDate,
        LocalDateTime endDate
) { }
