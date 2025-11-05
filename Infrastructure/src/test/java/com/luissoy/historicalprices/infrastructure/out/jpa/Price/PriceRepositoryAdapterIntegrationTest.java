package com.luissoy.historicalprices.infrastructure.out.jpa.Price;

import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import com.luissoy.historicalprices.infrastructure.out.jpa.Product.JpaProductRepository;
import com.luissoy.historicalprices.infrastructure.out.jpa.Product.ProductEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({PriceRepositoryAdapter.class, PriceEntityMapper.class})
class PriceRepositoryAdapterIntegrationTest {

    @Autowired
    private PriceRepositoryAdapter adapter;

    @Autowired
    private JpaPriceRepository jpa;

    @Autowired
    private JpaProductRepository productJpa;

    @Test
    void shouldSaveAndFindPrice() {
        ProductEntity product = productJpa.save(new ProductEntity(null, "Tablet", "10 inch"));
        Price price = PriceFactory.createPrice(
                null,
                new ProductId(product.getId()),
                new Money(BigDecimal.valueOf(9.99), new Currency("EUR")),
                new DateRange(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)),
                List.of()
        );

        Price saved = adapter.save(price);
        assertThat(saved.id()).isNotNull();

        Optional<Price> found = adapter.findById(saved.id());
        assertThat(found).isPresent();
        assertThat(found.get().value().amount()).isEqualTo(BigDecimal.valueOf(9.99));
    }

    @Test
    void shouldFindByProductId() {
        ProductEntity product = productJpa.save(new ProductEntity(null, "Tablet", "10 inch"));
        PriceEntity price = jpa.save(new PriceEntity(null, BigDecimal.TEN, "EUR",
                LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5), product.getId()));

        List<Price> prices = adapter.findByProductId(new ProductId(price.getProductId()));

        assertThat(prices).hasSize(1);
        assertThat(prices.get(0).productId().getValue()).isEqualTo(price.getProductId());
    }

    @Test
    void shouldFindByProductIdAndDate() {
        ProductEntity product = productJpa.save(new ProductEntity(null, "Tablet", "10 inch"));
        LocalDateTime now = LocalDateTime.now();
        jpa.save(new PriceEntity(null, BigDecimal.valueOf(15), "USD",
                now.minusDays(2), now.plusDays(2), product.getId()));

        Optional<Price> price = adapter.findByProductIdAndDate(new ProductId(product.getId()), now);

        assertThat(price).isPresent();
        assertThat(price.get().value().amount()).isEqualTo(BigDecimal.valueOf(15));
    }
}
