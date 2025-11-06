package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.mapper.PriceMapper;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResult;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.Product;
import com.luissoy.historicalprices.domain.product.ProductRepository;
import com.luissoy.historicalprices.domain.product.exception.ProductNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductDescription;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import com.luissoy.historicalprices.domain.product.valueobject.ProductName;
import com.luissoy.historicalprices.domain.shared.valueobject.Currency;
import com.luissoy.historicalprices.domain.shared.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.valueobject.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    private PriceRepository priceRepository;
    private ProductRepository productRepository;
    private PriceMapper priceMapper;
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        priceRepository = mock(PriceRepository.class);
        productRepository = mock(ProductRepository.class);
        priceMapper = mock(PriceMapper.class);

        priceService = new PriceService(priceRepository, productRepository, priceMapper);
    }

    @Test
    @DisplayName("should add new price when no overlapping exists")
    void addPrice() {
        ProductId productId = new ProductId(1L);
        Product product = new Product(productId, new ProductName("Test Product"), new ProductDescription("Desc"));
        PriceCommand command = new PriceCommand(
                BigDecimal.valueOf(10.5),
                "EUR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        when(productRepository.findById(any(ProductId.class))).thenReturn(Optional.of(product));
        when(priceRepository.findByProductId(any(ProductId.class))).thenReturn(List.of());

        when(priceRepository.save(any(Price.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(priceMapper.toDto(any(Price.class))).thenReturn(
                new PriceResult(1L, 1L, BigDecimal.valueOf(10.5), "EUR", command.initDate(), command.endDate())
        );

        PriceResult response = priceService.addPrice(1L, command);

        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(1L);
        verify(priceRepository).save(any(Price.class));
        verify(priceMapper, times(1)).toDto(any(Price.class));
    }

    @Test
    @DisplayName("should throw exception when product not found")
    void addPrice_throwsWhenProductNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        PriceCommand command = new PriceCommand(
                BigDecimal.valueOf(10),
                "EUR",
                LocalDate.now(),
                LocalDate.now().plusDays(1)
        );

        assertThrows(ProductNotFoundException.class, () -> priceService.addPrice(1L, command));
        verify(priceRepository, never()).save(any());
    }

    @Test
    @DisplayName("should return active price for given date")
    void getActivePrice() {
        ProductId productId = new ProductId(1L);
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                productId,
                new Money(BigDecimal.valueOf(20), new Currency("EUR")),
                new DateRange(LocalDate.now().minusDays(5), LocalDate.now().plusDays(5)),
                List.of()
        );

        when(priceRepository.findByProductIdAndDate(eq(productId), any())).thenReturn(Optional.of(price));
        when(priceMapper.toDto(price)).thenReturn(
                new PriceResult(1L, 1L, BigDecimal.valueOf(20), "EUR", price.dateRange().start(), price.dateRange().end())
        );

        PriceResult response = priceService.getActivePrice(1L, LocalDate.now());

        assertThat(response).isNotNull();
        assertThat(response.value()).isEqualTo(BigDecimal.valueOf(20));
    }

    @Test
    @DisplayName("should throw exception when active price not found")
    void getActivePrice_notFound() {
        ProductId productId = new ProductId(1L);
        when(priceRepository.findByProductIdAndDate(eq(productId), any())).thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () -> priceService.getActivePrice(1L, LocalDate.now()));
    }

    @Test
    @DisplayName("should return price history for a product")
    void getPriceHistory() {
        ProductId productId = new ProductId(1L);
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                productId,
                new Money(BigDecimal.TEN, new Currency("EUR")),
                new DateRange(LocalDate.now().minusDays(10), LocalDate.now().minusDays(5)),
                List.of()
        );
        List<Price> prices = List.of(
                price
        );

        when(priceRepository.findByProductId(productId)).thenReturn(prices);
        when(productRepository.findById(productId)).thenReturn(
                Optional.of(new Product(productId, new ProductName("Test"), new ProductDescription("Desc")))
        );
        when(priceMapper.toDto(any())).thenReturn(
                new PriceResult(1L, 1L, BigDecimal.TEN, "EUR", prices.get(0).dateRange().start(), prices.get(0).dateRange().end())
        );
        when(priceMapper.toPriceHistoryDto(any(), any())).thenReturn(
                new PriceHistoryResult(1L, "Test", "Desc", List.of(
                        new PriceResult(1L, 1L, BigDecimal.TEN, "EUR", prices.get(0).dateRange().start(), prices.get(0).dateRange().end())
                ))
        );

        PriceHistoryResult response = priceService.getPriceHistory(1L);

        assertThat(response).isNotNull();
        assertThat(response.prices()).hasSize(1);
        assertThat(response.productId()).isEqualTo(1L);
    }
}
