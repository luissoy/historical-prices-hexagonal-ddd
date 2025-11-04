package com.luissoy.historicalprices.application.price.dto;

import java.util.List;

public record PriceHistoryResponse(Long productId, List<PriceResponse> prices) { }
