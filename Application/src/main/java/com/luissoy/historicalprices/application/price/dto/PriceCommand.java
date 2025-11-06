package com.luissoy.historicalprices.application.price.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceCommand(
        BigDecimal value,
        String currencyCode,
        LocalDate initDate,
        LocalDate endDate
) { }
