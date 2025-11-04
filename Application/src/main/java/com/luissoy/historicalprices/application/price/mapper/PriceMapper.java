package com.luissoy.historicalprices.application.price.mapper;

import com.luissoy.historicalprices.application.price.dto.PriceResponse;
import com.luissoy.historicalprices.domain.price.Price;

public interface PriceMapper {
    PriceResponse toDto(Price price);
}
