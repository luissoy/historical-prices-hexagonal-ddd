package com.luissoy.historicalprices.infrastructure.out.persistence.price;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceRepositoryAdapterIntegrationTest {

    @Autowired
    private PriceRepositoryAdapter priceRepository;

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void setup() {
        jdbc.execute("DELETE FROM prices");
        jdbc.execute("DELETE FROM products");
        jdbc.update("INSERT INTO products (id, name, description) VALUES (1, 'Test Product', 'Desc')");
        jdbc.update("INSERT INTO prices (product_id, price_value, currency_code, init_date, end_date) VALUES " +
                "(1, 99.99, 'EUR', '2024-01-01', '2024-06-30')," +
                "(1, 109.99, 'EUR', '2024-07-01', NULL)");
    }

    @Test
    void save_and_findById_shouldWork() {
        Price price = new Price(
                null,
                new ProductId(1L),
                new com.luissoy.historicalprices.domain.price.valueobject.Money(
                        BigDecimal.valueOf(150),
                        new com.luissoy.historicalprices.domain.price.valueobject.Currency("EUR")
                ),
                new com.luissoy.historicalprices.domain.price.valueobject.DateRange(
                        LocalDate.of(2025,1,1),
                        LocalDate.of(2025,12,31)
                )
        );

        Price saved = priceRepository.save(price);

        Optional<Price> found = priceRepository.findById(saved.id());
        assertThat(found).isPresent();
        assertThat(found.get().value().amount()).isEqualByComparingTo(BigDecimal.valueOf(150));
    }

    @Test
    void findByProductId_shouldReturnAllPricesForProduct() {
        List<Price> prices = priceRepository.findByProductId(new ProductId(1L));
        assertThat(prices).hasSize(2);
        assertThat(prices).allMatch(p -> p.productId().getValue().equals(1L));
    }

    @Test
    void findByProductIdAndDate_shouldReturnCorrectPrice() {
        Optional<Price> price = priceRepository.findByProductIdAndDate(
                new ProductId(1L),
                LocalDate.of(2024,3,1)
        );

        assertThat(price).isPresent();
        assertThat(price.get().value().amount()).isEqualByComparingTo(BigDecimal.valueOf(99.99));
    }

    @Test
    void delete_shouldRemovePrice() {
        Price price = new Price(
                null,
                new ProductId(1L),
                new com.luissoy.historicalprices.domain.price.valueobject.Money(
                        BigDecimal.valueOf(200),
                        new com.luissoy.historicalprices.domain.price.valueobject.Currency("EUR")
                ),
                new com.luissoy.historicalprices.domain.price.valueobject.DateRange(
                        LocalDate.of(2025,1,1),
                        LocalDate.of(2025,12,31)
                )
        );

        Price saved = priceRepository.save(price);
        priceRepository.delete(saved.id());

        List<Price> prices = priceRepository.findByProductId(new ProductId(1L));
        assertThat(prices).noneMatch(p -> p.id().equals(saved.id()));
    }
}
