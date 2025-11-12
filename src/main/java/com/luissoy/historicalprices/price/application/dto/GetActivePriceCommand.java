package com.luissoy.historicalprices.price.application.dto;

import java.time.LocalDate;

public record GetActivePriceCommand(
        Long productId,
        LocalDate applicationDate
) { }