package com.luissoy.historicalprices.infrastructure.out.persistence.price;

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