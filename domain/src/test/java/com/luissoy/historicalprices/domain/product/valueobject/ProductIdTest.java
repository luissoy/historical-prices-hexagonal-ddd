package com.luissoy.historicalprices.domain.product.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductIdTest {

    @Test
    @DisplayName("should create valid ProductId when value is not null")
    void shouldCreateValidProductId() {
        ProductId id = new ProductId(100L);

        assertThat(id.getValue()).isEqualTo(100L);
        assertThat(id.toString()).isEqualTo("100");
    }

    @Test
    @DisplayName("should throw ValidationException when value is null")
    void shouldThrowWhenValueIsNull() {
        assertThrows(ValidationException.class, () -> new ProductId(null));
    }

    @Test
    @DisplayName("should be equal when values are the same")
    void shouldBeEqualWhenSameValue() {
        ProductId id1 = new ProductId(1L);
        ProductId id2 = new ProductId(1L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("should not be equal when values are different")
    void shouldNotBeEqualWhenDifferentValue() {
        ProductId id1 = new ProductId(1L);
        ProductId id2 = new ProductId(2L);

        assertThat(id1).isNotEqualTo(id2);
    }
}
