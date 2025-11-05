package com.luissoy.historicalprices.infrastructure.config;

import com.fasterxml.jackson.databind.Module;
import com.luissoy.historicalprices.application.price.PriceService;
import com.luissoy.historicalprices.application.price.mapper.PriceMapper;
import com.luissoy.historicalprices.application.price.port.in.PriceUseCase;
import com.luissoy.historicalprices.application.product.ProductService;
import com.luissoy.historicalprices.application.product.mapper.ProductMapper;
import com.luissoy.historicalprices.application.product.port.in.ProductUseCase;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.PriceApiMapper;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.ProductApiMapper;
import com.luissoy.historicalprices.infrastructure.out.jpa.Price.PriceEntityMapper;
import com.luissoy.historicalprices.infrastructure.out.jpa.Product.ProductEntityMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public ProductApiMapper productApiMapper() {
        return new ProductApiMapper();
    }

    @Bean
    public PriceApiMapper priceApiMapper() {
        return new PriceApiMapper();
    }

    @Bean
    public PriceEntityMapper priceEntityMapper() {
        return new PriceEntityMapper();
    }

    @Bean
    public ProductEntityMapper productEntityMapper() {
        return new ProductEntityMapper();
    }

    @Bean
    public ProductMapper productMapper() {
        return new ProductMapper();
    }

    @Bean
    public PriceMapper priceMapper() {
        return new PriceMapper();
    }

    @Bean
    public ProductUseCase productUseCase(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        return new ProductService(productRepository, productMapper);
    }

    @Bean
    public PriceUseCase priceUseCase(
            PriceRepository priceRepository,
            ProductRepository productRepository,
            PriceMapper priceMapper
    ) {
        return new PriceService(priceRepository, productRepository, priceMapper);
    }


    @Bean
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }
}