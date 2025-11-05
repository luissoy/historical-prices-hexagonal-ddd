package com.luissoy.historicalprices.infrastructure.in.rest.controller;

import com.luissoy.historicalprices.api.ProductsApi;
import com.luissoy.historicalprices.api.model.ProductRequest;
import com.luissoy.historicalprices.api.model.ProductResponse;
import com.luissoy.historicalprices.api.model.ProductWithPricesResponse;
import com.luissoy.historicalprices.api.model.PriceRequest;
import com.luissoy.historicalprices.api.model.PriceResponse;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.application.price.port.in.PriceUseCase;
import com.luissoy.historicalprices.application.product.dto.ProductCommand;
import com.luissoy.historicalprices.application.product.dto.ProductResult;
import com.luissoy.historicalprices.application.product.port.in.ProductUseCase;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.PriceApiMapper;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.ProductApiMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api")
public class ProductsController implements ProductsApi {

    private final ProductUseCase productService;
    private final PriceUseCase priceService;
    private final ProductApiMapper productApiMapper;
    private final PriceApiMapper priceApiMapper;

    public ProductsController(
            ProductUseCase productService,
            PriceUseCase priceService,
            ProductApiMapper productApiMapper,
            PriceApiMapper priceApiMapper
    ) {
        this.productService = productService;
        this.priceService = priceService;
        this.productApiMapper = productApiMapper;
        this.priceApiMapper = priceApiMapper;
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest request) {
        ProductCommand command = productApiMapper.toProductCommand(request);
        ProductResult result = productService.createProduct(command);
        ProductResponse response = productApiMapper.toProductResponse(result);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PriceResponse> addPrice(Long productId, PriceRequest request) {
        PriceCommand command = priceApiMapper.toPriceCommand(request);
        PriceResult result = priceService.addPrice(productId, command);
        PriceResponse response = priceApiMapper.toPriceResponse(result);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<ProductWithPricesResponse> getProductPriceHistory(Long productId) {
        PriceHistoryResponse priceHistoryResponse = priceService.getPriceHistory(productId);
        ProductWithPricesResponse response = priceApiMapper.toProductWithPricesResponse(priceHistoryResponse);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PriceResponse> getCurrentPrice(Long productId, OffsetDateTime date) {
        PriceResult result = priceService.getActivePrice(productId, date.toLocalDateTime());
        PriceResponse response = priceApiMapper.toPriceResponse(result);
        return ResponseEntity.ok(response);
    }
}