package com.luissoy.historicalprices.price.infrastructure.persistance;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceEntity(
        Long id,
        BigDecimal value,
        String currencyCode,
        LocalDate initDate,
        LocalDate endDate,
        Long productId
) {}