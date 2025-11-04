package com.luissoy.historicalprices.domain.price.exception;

import com.luissoy.historicalprices.domain.shared.exception.NotFoundException;

public class PriceNotFoundException extends NotFoundException {
    public PriceNotFoundException(Long id) {
        super("Price with id " + id + " not found");
    }
}
