package com.luissoy.historicalprices.price.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    @DisplayName("should create valid Money when amount and currency are correct")
    void shouldCreateValidMoney() {
        Currency eur = new Currency("EUR");
        Money money = new Money(BigDecimal.valueOf(99.99), eur);

        assertThat(money.amount()).isEqualByComparingTo("99.99");
        assertThat(money.currency()).isEqualTo(eur);
    }

    @Test
    @DisplayName("should throw ValidationException when amount is null")
    void shouldThrowWhenAmountIsNull() {
        Currency eur = new Currency("EUR");
        assertThrows(ValidationException.class, () -> new Money(null, eur));
    }

    @Test
    @DisplayName("should throw ValidationException when amount is negative")
    void shouldThrowWhenAmountIsNegative() {
        Currency eur = new Currency("EUR");
        assertThrows(ValidationException.class, () -> new Money(BigDecimal.valueOf(-10), eur));
    }

    @Test
    @DisplayName("should allow amount equal to zero")
    void shouldAllowZeroAmount() {
        Currency eur = new Currency("EUR");
        Money money = new Money(BigDecimal.ZERO, eur);

        assertThat(money.amount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("should return true when currencies are the same")
    void shouldReturnTrueWhenSameCurrency() {
        Currency eur1 = new Currency("EUR");
        Currency eur2 = new Currency("EUR");

        Money m1 = new Money(BigDecimal.TEN, eur1);
        Money m2 = new Money(BigDecimal.ONE, eur2);

        assertThat(m1.hasSameCurrency(m2)).isTrue();
    }

    @Test
    @DisplayName("should return false when currencies are different")
    void shouldReturnFalseWhenDifferentCurrency() {
        Money eur = new Money(BigDecimal.TEN, new Currency("EUR"));
        Money usd = new Money(BigDecimal.TEN, new Currency("USD"));

        assertThat(eur.hasSameCurrency(usd)).isFalse();
    }
}
