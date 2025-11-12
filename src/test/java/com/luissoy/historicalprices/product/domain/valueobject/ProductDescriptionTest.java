package com.luissoy.historicalprices.product.domain.valueobject;

import com.luissoy.historicalprices.product.domain.exception.InvalidProductDescriptionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductDescriptionTest {

    @Test
    @DisplayName("should create valid ProductDescription when value is not null or blank")
    void shouldCreateValidProductDescription() {
        ProductDescription description = new ProductDescription("A powerful gaming laptop");
        assertThat(description.value()).isEqualTo("A powerful gaming laptop");
    }

    @Test
    @DisplayName("should throw exception when description is null")
    void shouldThrowWhenDescriptionIsNull() {
        assertThrows(InvalidProductDescriptionException.class, () -> new ProductDescription(null));
    }

    @Test
    @DisplayName("should throw exception when description is blank")
    void shouldThrowWhenDescriptionIsBlank() {
        assertThrows(InvalidProductDescriptionException.class, () -> new ProductDescription("   "));
    }

    @Test
    @DisplayName("should consider two ProductDescriptions with same value as equal")
    void shouldBeEqualWhenSameValue() {
        ProductDescription desc1 = new ProductDescription("Smartphone with 128GB storage");
        ProductDescription desc2 = new ProductDescription("Smartphone with 128GB storage");

        assertThat(desc1).isEqualTo(desc2);
        assertThat(desc1.hashCode()).isEqualTo(desc2.hashCode());
    }

    @Test
    @DisplayName("should have a readable toString value")
    void shouldHaveReadableToString() {
        ProductDescription description = new ProductDescription("4K OLED TV");
        assertThat(description.toString()).contains("4K OLED TV");
    }
}
