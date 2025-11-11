package com.luissoy.historicalprices.domain.price.valueobject;

import com.luissoy.historicalprices.domain.price.valueobject.DateRange;
import com.luissoy.historicalprices.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateRangeTest {

    @Test
    @DisplayName("should create DateRange when start and end are valid")
    void shouldCreateValidDateRange() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);

        DateRange range = new DateRange(start, end);

        assertThat(range.start()).isEqualTo(start);
        assertThat(range.end()).isEqualTo(end);
    }

    @Test
    @DisplayName("should throw ValidationException when start is null")
    void shouldThrowExceptionWhenStartIsNull() {
        LocalDate end = LocalDate.of(2024, 12, 31);
        assertThrows(ValidationException.class, () -> new DateRange(null, end));
    }

    @Test
    @DisplayName("should allow null end date and include future dates")
    void shouldAllowNullEndDate() {
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                null
        );
        LocalDate futureDate = LocalDate.of(2030, 1, 1);

        assertThat(range.includes(futureDate)).isTrue();
    }

    @Test
    @DisplayName("should throw ValidationException when end is before start")
    void shouldThrowExceptionWhenEndIsBeforeStart() {
        LocalDate start = LocalDate.of(2024, 12, 31);
        LocalDate end = LocalDate.of(2024, 1, 1);
        assertThrows(ValidationException.class, () -> new DateRange(start, end));
    }

    @Test
    @DisplayName("should return true when date is within range")
    void shouldReturnTrueWhenDateIsWithinRange() {
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31)
        );
        LocalDate testDate = LocalDate.of(2024, 6, 15);

        assertThat(range.includes(testDate)).isTrue();
    }

    @Test
    @DisplayName("should return false when date is outside range")
    void shouldReturnFalseWhenDateIsOutsideRange() {
        DateRange range = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30)
        );
        LocalDate testDate = LocalDate.of(2024, 12, 1);

        assertThat(range.includes(testDate)).isFalse();
    }

    @Test
    @DisplayName("should detect overlapping date ranges")
    void shouldDetectOverlappingRanges() {
        DateRange range1 = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 6, 30)
        );
        DateRange range2 = new DateRange(
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2024, 12, 31)
        );

        assertThat(range1.overlaps(range2)).isTrue();
    }

    @Test
    @DisplayName("should detect non-overlapping date ranges")
    void shouldDetectNonOverlappingRanges() {
        DateRange range1 = new DateRange(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
        );
        DateRange range2 = new DateRange(
                LocalDate.of(2024, 4, 1),
                LocalDate.of(2024, 6, 30)
        );

        assertThat(range1.overlaps(range2)).isFalse();
    }
}
