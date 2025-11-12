package com.luissoy.historicalprices.application.price.dto;

import java.time.LocalDate;

public record GetActivePriceCommand(
        Long productId,
        LocalDate applicationDate
) { }