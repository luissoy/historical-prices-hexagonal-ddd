package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.mapper.PriceMapper;
import com.luissoy.historicalprices.application.price.port.out.LoadPricePort;
import com.luissoy.historicalprices.application.product.port.out.LoadProductPort;
import com.luissoy.historicalprices.application.price.port.out.SavePricePort;
import com.luissoy.historicalprices.application.price.dto.PriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceHistoryResponse;
import com.luissoy.historicalprices.application.price.dto.PriceResponse;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceFactory;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.price.valueobject.PriceId;
import com.luissoy.historicalprices.domain.product.Product;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PriceServiceTest {

    private LoadProductPort loadProductPort;
    private LoadPricePort loadPricePort;
    private SavePricePort savePricePort;
    private PriceMapper priceMapper;
    private PriceService priceService;

    @BeforeEach
    void setUp() {
        loadProductPort = mock(LoadProductPort.class);
        loadPricePort = mock(LoadPricePort.class);
        savePricePort = mock(SavePricePort.class);
        priceMapper = mock(PriceMapper.class);

        priceService = new PriceService(loadProductPort, loadPricePort, savePricePort, priceMapper);
    }

    @Test
    @DisplayName("should add new price when no overlapping exists")
    void addPrice() {
        ProductId productId = new ProductId(1L);
        Product product = new Product(productId, new ProductName("Test Product"), new ProductDescription("Desc"));
        PriceCommand command = new PriceCommand(
                BigDecimal.valueOf(10.5),
                "EUR",
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 0, 0)
        );

        when(loadProductPort.findById(any(ProductId.class))).thenReturn(Optional.of(product));
        when(loadPricePort.findByProductId(any(ProductId.class))).thenReturn(List.of());

        when(savePricePort.save(any(Price.class))).thenAnswer(invocation -> invocation.getArgument(0));

        when(priceMapper.toDto(any(Price.class))).thenReturn(
                new PriceResponse(1L, 1L, BigDecimal.valueOf(10.5), "EUR", command.initDate(), command.endDate())
        );

        PriceResponse response = priceService.addPrice(1L, command);

        assertThat(response).isNotNull();
        assertThat(response.productId()).isEqualTo(1L);
        verify(savePricePort).save(any(Price.class));
        verify(priceMapper, times(1)).toDto(any(Price.class));
    }

    @Test
    @DisplayName("should throw exception when product not found")
    void addPrice_throwsWhenProductNotFound() {
        when(loadProductPort.findById(any())).thenReturn(Optional.empty());

        PriceCommand command = new PriceCommand(
                BigDecimal.valueOf(10),
                "EUR",
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );

        assertThrows(ProductNotFoundException.class, () -> priceService.addPrice(1L, command));
        verify(savePricePort, never()).save(any());
    }

    @Test
    @DisplayName("should return active price for given date")
    void getActivePrice() {
        ProductId productId = new ProductId(1L);
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                productId,
                new Money(BigDecimal.valueOf(20), new Currency("EUR")),
                new DateRange(LocalDateTime.now().minusDays(5), LocalDateTime.now().plusDays(5)),
                List.of()
        );

        when(loadPricePort.findByProductIdAndDate(eq(productId), any())).thenReturn(Optional.of(price));
        when(priceMapper.toDto(price)).thenReturn(
                new PriceResponse(1L, 1L, BigDecimal.valueOf(20), "EUR", price.dateRange().start(), price.dateRange().end())
        );

        PriceResponse response = priceService.getActivePrice(1L, LocalDateTime.now());

        assertThat(response).isNotNull();
        assertThat(response.value()).isEqualTo(BigDecimal.valueOf(20));
    }

    @Test
    @DisplayName("should throw exception when active price not found")
    void getActivePrice_notFound() {
        ProductId productId = new ProductId(1L);
        when(loadPricePort.findByProductIdAndDate(eq(productId), any())).thenReturn(Optional.empty());

        assertThrows(PriceNotFoundException.class, () -> priceService.getActivePrice(1L, LocalDateTime.now()));
    }

    @Test
    @DisplayName("should return price history for a product")
    void getPriceHistory() {
        ProductId productId = new ProductId(1L);
        Price price = PriceFactory.createPrice(
                new PriceId(1L),
                productId,
                new Money(BigDecimal.TEN, new Currency("EUR")),
                new DateRange(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(5)),
                List.of()
        );
        List<Price> prices = List.of(
                price
        );

        when(loadPricePort.findByProductId(productId)).thenReturn(prices);
        when(priceMapper.toDto(any())).thenReturn(
                new PriceResponse(1L, 1L, BigDecimal.TEN, "EUR", prices.get(0).dateRange().start(), prices.get(0).dateRange().end())
        );

        PriceHistoryResponse response = priceService.getPriceHistory(1L);

        assertThat(response).isNotNull();
        assertThat(response.prices()).hasSize(1);
        assertThat(response.productId()).isEqualTo(1L);
    }
}
