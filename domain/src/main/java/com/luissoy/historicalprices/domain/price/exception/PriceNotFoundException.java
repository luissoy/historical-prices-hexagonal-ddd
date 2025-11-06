package com.luissoy.historicalprices.domain.price.exception;

import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.exception.NotFoundException;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException(PriceId id) {
        super("Price with id " + id.toString() + " not found");
    }

    public PriceNotFoundException(ProductId id) {
        super("Price for product with id " + id.toString() + " not found");
    }
}
