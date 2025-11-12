package com.luissoy.historicalprices.product.domain.valueobject;

import com.luissoy.historicalprices.product.domain.exception.InvalidProductNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductNameTest {

    @Test
    @DisplayName("should create valid ProductName when value is not null or blank")
    void shouldCreateValidProductName() {
        ProductName name = new ProductName("Laptop");
        assertThat(name.value()).isEqualTo("Laptop");
    }

    @Test
    @DisplayName("should throw exception when name is null")
    void shouldThrowWhenNameIsNull() {
        assertThrows(InvalidProductNameException.class, () -> new ProductName(null));
    }

    @Test
    @DisplayName("should throw exception when name is blank")
    void shouldThrowWhenNameIsBlank() {
        assertThrows(InvalidProductNameException.class, () -> new ProductName("   "));
    }

    @Test
    @DisplayName("should consider two ProductNames with same value as equal")
    void shouldBeEqualWhenSameValue() {
        ProductName name1 = new ProductName("Phone");
        ProductName name2 = new ProductName("Phone");

        assertThat(name1).isEqualTo(name2);
        assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
    }

    @Test
    @DisplayName("should have a readable toString value")
    void shouldHaveReadableToString() {
        ProductName name = new ProductName("Tablet");
        assertThat(name.toString()).contains("Tablet");
    }
}
