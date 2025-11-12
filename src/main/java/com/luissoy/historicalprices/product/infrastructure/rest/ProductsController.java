package com.luissoy.historicalprices.product.infrastructure.rest;

import com.luissoy.historicalprices.api.ProductsApi;
import com.luissoy.historicalprices.api.model.*;

import com.luissoy.historicalprices.price.application.AddProductPriceUseCase;
import com.luissoy.historicalprices.price.application.GetProductActivePriceUseCase;
import com.luissoy.historicalprices.price.application.GetProductPriceHistoryUseCase;
import com.luissoy.historicalprices.price.application.dto.AddPriceCommand;
import com.luissoy.historicalprices.price.application.dto.GetActivePriceCommand;
import com.luissoy.historicalprices.price.application.dto.PriceResult;
import com.luissoy.historicalprices.price.infrastructure.rest.PriceApiMapper;
import com.luissoy.historicalprices.product.application.CreateProductUseCase;
import com.luissoy.historicalprices.product.application.GetProductUseCase;
import com.luissoy.historicalprices.product.application.dto.CreateProductCommand;
import com.luissoy.historicalprices.product.application.dto.ProductResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductsController implements ProductsApi {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final AddProductPriceUseCase addProductPriceUseCase;
    private final GetProductPriceHistoryUseCase getProductPriceHistoryUseCase;
    private final GetProductActivePriceUseCase getProductActivePriceUseCase;
    private final ProductApiMapper productApiMapper;
    private final PriceApiMapper priceApiMapper;

    public ProductsController(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase,
            AddProductPriceUseCase addProductPriceUseCase,
            GetProductPriceHistoryUseCase getProductPriceHistoryUseCase,
            GetProductActivePriceUseCase getProductActivePriceUseCase,
            ProductApiMapper productApiMapper,
            PriceApiMapper priceApiMapper
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.addProductPriceUseCase = addProductPriceUseCase;
        this.getProductPriceHistoryUseCase = getProductPriceHistoryUseCase;
        this.getProductActivePriceUseCase = getProductActivePriceUseCase;
        this.productApiMapper = productApiMapper;
        this.priceApiMapper = priceApiMapper;
    }

    @Override
    public ResponseEntity<ProductResponse> createProduct(ProductRequest request) {
        CreateProductCommand command = productApiMapper.toProductCommand(request);
        ProductResult result = createProductUseCase.execute(command);
        ProductResponse response = productApiMapper.toProductResponse(result);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PriceResponse> addPrice(Long productId, PriceRequest request) {
        AddPriceCommand command = priceApiMapper.toPriceCommand(productId, request);
        PriceResult result = addProductPriceUseCase.execute(command);
        PriceResponse response = priceApiMapper.toPriceResponse(result);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<ProductWithPricesResponse> getProductPriceHistory(Long productId) {
        List<PriceResult> results = getProductPriceHistoryUseCase.execute(productId);
        ProductResult productResult = getProductUseCase.execute(productId);
        ProductWithPricesResponse response = priceApiMapper.toProductWithPricesResponse(productResult, results);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CurrentPriceResponse> getCurrentPrice(Long productId, LocalDate date) {
        GetActivePriceCommand command = new GetActivePriceCommand(productId, date);
        PriceResult result = getProductActivePriceUseCase.execute(command);
        CurrentPriceResponse response = priceApiMapper.toCurrentPriceResponse(result);
        return ResponseEntity.ok(response);
    }
}
