package com.luissoy.historicalprices.price.infrastructure.persistance;

import com.luissoy.historicalprices.price.domain.PriceRepository;
import com.luissoy.historicalprices.price.domain.Price;
import com.luissoy.historicalprices.price.domain.valueobject.PriceId;
import com.luissoy.historicalprices.product.domain.valueobject.ProductId;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class PriceRepositoryAdapter implements PriceRepository {

    private static final String SELECT_BASE = """
        SELECT id, price_value, currency_code, init_date, end_date, product_id
        FROM prices
    """;

    private static final String SELECT_BY_ID = SELECT_BASE + " WHERE id = ?";
    private static final String SELECT_BY_PRODUCT_ID = SELECT_BASE + " WHERE product_id = ?";
    private static final String SELECT_BY_PRODUCT_AND_DATE =
            SELECT_BASE + " WHERE product_id = ? AND init_date <= ? AND (end_date IS NULL OR end_date >= ?)";

    private static final String UPDATE_PRICE = """
        UPDATE prices
        SET price_value = ?, currency_code = ?, init_date = ?, end_date = ?, product_id = ?
        WHERE id = ?
    """;

    private static final String DELETE_PRICE = "DELETE FROM prices WHERE id = ?";

    private final JdbcTemplate jdbc;
    private final SimpleJdbcInsert priceInsert;
    private final PriceEntityMapper mapper;

    public PriceRepositoryAdapter(JdbcTemplate jdbc, PriceEntityMapper mapper, SimpleJdbcInsert priceInsert) {
        this.jdbc = jdbc;
        this.priceInsert = priceInsert;
        this.mapper = mapper;
    }

    @Override
    public Optional<Price> findById(PriceId id) {
        List<Price> result = jdbc.query(
                SELECT_BY_ID,
                (rs, rowNum) -> mapper.resultSetToDomain(rs),
                id.getValue()
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Price> findByProductId(ProductId productId) {
        return jdbc.query(
                SELECT_BY_PRODUCT_ID,
                (rs, rowNum) -> mapper.resultSetToDomain(rs),
                productId.getValue()
        );
    }

    @Override
    public Optional<Price> findByProductIdAndDate(ProductId productId, LocalDate date) {
        List<Price> result = jdbc.query(
                SELECT_BY_PRODUCT_AND_DATE,
                (rs, rowNum) -> mapper.resultSetToDomain(rs),
                productId.getValue(),
                date,
                date
        );
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    @Transactional
    public Price save(Price price) {
        Long id = price.id() != null ? price.id().getValue() : null;

        if (id == null) {
            Map<String, Object> params = new HashMap<>();
            params.put("price_value", price.value().amount());
            params.put("currency_code", price.value().currency().code());
            params.put("init_date", price.dateRange().start());
            params.put("end_date", price.dateRange().end());
            params.put("product_id", price.productId().getValue());

            Number key = priceInsert.executeAndReturnKey(params);
            id = key.longValue();
        } else {
            jdbc.update(
                    UPDATE_PRICE,
                    price.value().amount(),
                    price.value().currency().code(),
                    price.dateRange().start(),
                    price.dateRange().end(),
                    price.productId().getValue(),
                    id
            );
        }

        return findById(new PriceId(id)).orElseThrow();
    }

    @Override
    @Transactional
    public void delete(PriceId id) {
        jdbc.update(DELETE_PRICE, id.getValue());
    }
}
