package com.luissoy.historicalprices.price.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PriceIdTest {

    @Test
    @DisplayName("should create PriceId with correct value")
    void shouldCreatePriceIdWithCorrectValue() {
        PriceId priceId = new PriceId(100L);

        assertThat(priceId.getValue()).isEqualTo(100L);
    }

    @Test
    @DisplayName("should consider two PriceIds with same value as equal")
    void shouldBeEqualWhenSameValue() {
        PriceId id1 = new PriceId(1L);
        PriceId id2 = new PriceId(1L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("should not be equal when values differ")
    void shouldNotBeEqualWhenDifferentValue() {
        PriceId id1 = new PriceId(1L);
        PriceId id2 = new PriceId(2L);

        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    @DisplayName("should return string representation matching value")
    void shouldReturnStringRepresentationMatchingValue() {
        PriceId id = new PriceId(42L);

        assertThat(id.toString()).isEqualTo("42");
    }
}
