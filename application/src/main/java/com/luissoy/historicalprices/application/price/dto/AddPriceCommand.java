package com.luissoy.historicalprices.application.price.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record AddPriceCommand(
        Long productId,
        BigDecimal value,
        String currencyCode,
        LocalDate initDate,
        LocalDate endDate
) { }

