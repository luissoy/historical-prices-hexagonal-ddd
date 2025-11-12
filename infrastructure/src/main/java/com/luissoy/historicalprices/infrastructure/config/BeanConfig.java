package com.luissoy.historicalprices.infrastructure.config;

import com.fasterxml.jackson.databind.Module;
import com.luissoy.historicalprices.application.price.GetProductPriceHistoryUseCase;
import com.luissoy.historicalprices.application.price.AddProductPriceUseCase;
import com.luissoy.historicalprices.application.price.GetProductActivePriceUseCase;
import com.luissoy.historicalprices.application.price.PriceMapper;
import com.luissoy.historicalprices.application.product.CreateProductUseCase;
import com.luissoy.historicalprices.application.product.GetProductUseCase;
import com.luissoy.historicalprices.application.product.ProductMapper;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.PriceApiMapper;
import com.luissoy.historicalprices.infrastructure.in.rest.mapper.ProductApiMapper;
import com.luissoy.historicalprices.infrastructure.out.persistence.price.PriceEntityMapper;
import com.luissoy.historicalprices.infrastructure.out.persistence.price.PriceRepositoryAdapter;
import com.luissoy.historicalprices.infrastructure.out.persistence.product.ProductDataAccess;
import com.luissoy.historicalprices.infrastructure.out.persistence.product.ProductEntityMapper;
import org.openapitools.jackson.nullable.JsonNullableModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

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
    public Module jsonNullableModule() {
        return new JsonNullableModule();
    }

    @Bean
    public CreateProductUseCase createProductUseCase(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        return new CreateProductUseCase(productRepository, productMapper);
    }

    @Bean
    public GetProductUseCase getProductUseCase(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        return new GetProductUseCase(productRepository, productMapper);
    }

    @Bean
    public AddProductPriceUseCase addPriceUseCase(
            ProductRepository productRepository,
            PriceMapper priceMapper
    ) {
        return new AddProductPriceUseCase(productRepository, priceMapper);
    }

    @Bean
    public GetProductActivePriceUseCase getActivePriceUseCase(
            PriceRepository priceRepository,
            PriceMapper priceMapper
    ) {
        return new GetProductActivePriceUseCase(priceRepository, priceMapper);
    }

    @Bean
    public GetProductPriceHistoryUseCase getPriceHistoryUseCase(
            ProductRepository productRepository,
            PriceMapper priceMapper
    ) {
        return new GetProductPriceHistoryUseCase(productRepository, priceMapper);
    }

    @Bean
    public ProductDataAccess productDataAccess(
            JdbcTemplate jdbc,
            ProductEntityMapper productEntityMapper
    ) {
        return new ProductDataAccess(
                jdbc,
                productEntityMapper,
                new SimpleJdbcInsert(jdbc)
                        .withTableName("PRODUCTS")
                        .usingGeneratedKeyColumns("id")
        );
    }

    @Bean
    public PriceRepositoryAdapter priceRepositoryAdapter(
            JdbcTemplate jdbc,
            PriceEntityMapper priceEntityMapper
    ) {
        return new PriceRepositoryAdapter(
                jdbc,
                priceEntityMapper,
                new SimpleJdbcInsert(jdbc)
                        .withTableName("prices")
                        .usingGeneratedKeyColumns("id")
        );
    }
}