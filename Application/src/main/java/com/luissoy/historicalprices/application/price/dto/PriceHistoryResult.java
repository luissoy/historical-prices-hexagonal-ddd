package com.luissoy.historicalprices.application.price.dto;

import java.util.List;

public record PriceHistoryResult(
        Long productId,
        String productName,
        String description,
        List<PriceResult> prices
) { }
