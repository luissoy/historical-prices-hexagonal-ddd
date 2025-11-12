package com.luissoy.historicalprices.domain.price.exception;

import com.luissoy.historicalprices.domain.shared.exception.NotFoundException;

public class OverlappingPriceException extends NotFoundException {
    public OverlappingPriceException() {
        super("Overlapping price periods detected");
    }
}