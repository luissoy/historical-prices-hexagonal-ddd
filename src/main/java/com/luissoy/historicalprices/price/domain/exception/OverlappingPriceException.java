package com.luissoy.historicalprices.price.domain.exception;

import com.luissoy.historicalprices.shared.domain.exception.NotFoundException;

public class OverlappingPriceException extends NotFoundException {
    public OverlappingPriceException() {
        super("Overlapping price periods detected");
    }
}