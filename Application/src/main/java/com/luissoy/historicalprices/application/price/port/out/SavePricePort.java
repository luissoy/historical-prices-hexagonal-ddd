package com.luissoy.historicalprices.application.price.port.out;

import com.luissoy.historicalprices.domain.price.Price;

public interface SavePricePort {
    Price save(Price price);
}
