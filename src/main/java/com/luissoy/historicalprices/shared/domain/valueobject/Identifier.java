package com.luissoy.historicalprices.shared.domain.valueobject;

import com.luissoy.historicalprices.shared.domain.exception.ValidationException;

import java.util.Objects;

public abstract class Identifier<T> {
    private final T value;

    protected Identifier(T value) {
        if (value == null) throw new ValidationException("Identifier value cannot be null");
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        return o != null
                && this.getClass() == o.getClass()
                && Objects.equals(this.value, ((Identifier<?>) o).value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}