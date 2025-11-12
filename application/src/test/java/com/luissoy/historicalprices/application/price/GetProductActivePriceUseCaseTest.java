package com.luissoy.historicalprices.application.price;

import com.luissoy.historicalprices.application.price.dto.GetActivePriceCommand;
import com.luissoy.historicalprices.application.price.dto.PriceResult;
import com.luissoy.historicalprices.domain.price.Price;
import com.luissoy.historicalprices.domain.price.PriceRepository;
import com.luissoy.historicalprices.domain.price.exception.PriceNotFoundException;
import com.luissoy.historicalprices.domain.product.valueobject.ProductId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetProductActivePriceUseCaseTest {

    private PriceRepository priceRepository;
    private PriceMapper priceMapper;
    private GetProductActivePriceUseCase useCase;

    @BeforeEach
    void setUp() {
        priceRepository = mock(PriceRepository.class);
        priceMapper = mock(PriceMapper.class);
        useCase = new GetProductActivePriceUseCase(priceRepository, priceMapper);
    }

    @Test
    void execute_should_return_mapped_price_when_active_price_exists() {
        Long productId = 1L;
        LocalDate applicationDate = LocalDate.of(2024, 6, 15);
        GetActivePriceCommand command = new GetActivePriceCommand(productId, applicationDate);

        Price activePrice = mock(Price.class);

        PriceResult expected = new PriceResult(
                10L,
                productId,
                new BigDecimal("19.99"),
                "EUR",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );

        when(priceRepository.findByProductIdAndDate(new ProductId(productId), applicationDate))
                .thenReturn(Optional.of(activePrice));
        when(priceMapper.toPriceResult(activePrice)).thenReturn(expected);

        PriceResult result = useCase.execute(command);

        assertThat(result).isSameAs(expected);
        verify(priceRepository).findByProductIdAndDate(new ProductId(productId), applicationDate);
        verify(priceMapper).toPriceResult(activePrice);
        verifyNoMoreInteractions(priceRepository, priceMapper);
    }

    @Test
    void execute_should_throw_when_no_active_price_found() {
        Long productId = 99L;
        LocalDate applicationDate = LocalDate.of(2024, 6, 15);
        GetActivePriceCommand command = new GetActivePriceCommand(productId, applicationDate);

        when(priceRepository.findByProductIdAndDate(new ProductId(productId), applicationDate))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(command))
                .isInstanceOf(PriceNotFoundException.class);

        verify(priceRepository).findByProductIdAndDate(new ProductId(productId), applicationDate);
        verify(priceMapper, never()).toPriceResult(any());
        verifyNoMoreInteractions(priceRepository, priceMapper);
    }
}