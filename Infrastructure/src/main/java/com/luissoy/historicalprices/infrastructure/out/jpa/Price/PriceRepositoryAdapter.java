package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class PriceRepositoryAdapter implements PriceRepository {

    private final JpaPriceRepository jpa;
    private final PriceEntityMapper mapper;

    public PriceRepositoryAdapter(JpaPriceRepository jpa, PriceEntityMapper mapper) {
        this.jpa = jpa;
        this.mapper = mapper;
    }

    @Override
    public Optional<Price> findById(PriceId id) {
        return jpa.findById(id.getValue()).map(mapper::toDomain);
    }

    @Override
    public List<Price> findByProductId(ProductId productId) {
        return jpa.findByProductId(productId.getValue()).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Price> findByProductIdAndDate(ProductId id, LocalDate date) {
        return jpa.findByProductIdAndDate(id.getValue(), date)
                .map(mapper::toDomain);
    }

    @Override
    public Price save(Price price) {
        PriceEntity entity = mapper.toEntity(price);
        PriceEntity saved = jpa.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void delete(PriceId id) {
        jpa.deleteById(id.getValue());
    }
}