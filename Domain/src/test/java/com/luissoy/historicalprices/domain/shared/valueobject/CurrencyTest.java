package com.luissoy.historicalprices.domain.shared.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CurrencyTest {

    @Test
    @DisplayName("should create currency when code is valid (three uppercase letters)")
    void shouldCreateCurrencyWhenCodeIsValid() {
        Currency currency = new Currency("USD");

        assertThat(currency).isNotNull();
        assertThat(currency.code()).isEqualTo("USD");
        assertThat(currency.toString()).contains("USD");
    }

    @Test
    @DisplayName("should throw ValidationException when code is null")
    void shouldThrowExceptionWhenCodeIsNull() {
        assertThrows(ValidationException.class, () -> new Currency(null));
    }

    @Test
    @DisplayName("should throw ValidationException when code is not three letters")
    void shouldThrowExceptionWhenCodeIsNotThreeLetters() {
        assertThrows(ValidationException.class, () -> new Currency("US"));
        assertThrows(ValidationException.class, () -> new Currency("USDA"));
    }

    @Test
    @DisplayName("should throw ValidationException when code is lowercase")
    void shouldThrowExceptionWhenCodeIsLowercase() {
        assertThrows(ValidationException.class, () -> new Currency("usd"));
    }

    @Test
    @DisplayName("should be equal when codes are the same")
    void shouldBeEqualWhenCodesAreTheSame() {
        Currency usd1 = new Currency("USD");
        Currency usd2 = new Currency("USD");

        assertThat(usd1).isEqualTo(usd2);
        assertThat(usd1.hashCode()).isEqualTo(usd2.hashCode());
    }

    @Test
    @DisplayName("should not be equal when codes are different")
    void shouldNotBeEqualWhenCodesAreDifferent() {
        Currency usd = new Currency("USD");
        Currency eur = new Currency("EUR");

        assertThat(usd).isNotEqualTo(eur);
    }
}
