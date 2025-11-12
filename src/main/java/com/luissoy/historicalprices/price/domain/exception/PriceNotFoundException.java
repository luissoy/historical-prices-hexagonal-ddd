package com.luissoy.historicalprices.price.domain.exception;

import com.luissoy.historicalprices.shared.domain.exception.NotFoundException;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException(PriceId id) {
        super("Price with id " + id.toString() + " not found");
    }

    public PriceNotFoundException(ProductId id) {
        super("Price for product with id " + id.toString() + " not found");
    }
}
