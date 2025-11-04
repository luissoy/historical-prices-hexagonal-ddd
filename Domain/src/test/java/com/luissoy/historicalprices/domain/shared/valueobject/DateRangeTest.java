package com.luissoy.historicalprices.domain.shared.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateRangeTest {

    @Test
    @DisplayName("should create DateRange when start and end are valid")
    void shouldCreateValidDateRange() {
        LocalDateTime start = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);

        DateRange range = new DateRange(start, end);

        assertThat(range.start()).isEqualTo(start);
        assertThat(range.end()).isEqualTo(end);
    }

    @Test
    @DisplayName("should throw ValidationException when start is null")
    void shouldThrowExceptionWhenStartIsNull() {
        LocalDateTime end = LocalDateTime.of(2024, 12, 31, 23, 59);
        assertThrows(ValidationException.class, () -> new DateRange(null, end));
    }

    @Test
    @DisplayName("should allow null end date and include future dates")
    void shouldAllowNullEndDate() {
        DateRange range = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                null
        );
        LocalDateTime futureDate = LocalDateTime.of(2030, 1, 1, 0, 0);

        assertThat(range.includes(futureDate)).isTrue();
    }

    @Test
    @DisplayName("should throw ValidationException when end is before start")
    void shouldThrowExceptionWhenEndIsBeforeStart() {
        LocalDateTime start = LocalDateTime.of(2024, 12, 31, 0, 0);
        LocalDateTime end = LocalDateTime.of(2024, 1, 1, 0, 0);
        assertThrows(ValidationException.class, () -> new DateRange(start, end));
    }

    @Test
    @DisplayName("should return true when date is within range")
    void shouldReturnTrueWhenDateIsWithinRange() {
        DateRange range = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 23, 59)
        );
        LocalDateTime testDate = LocalDateTime.of(2024, 6, 15, 12, 0);

        assertThat(range.includes(testDate)).isTrue();
    }

    @Test
    @DisplayName("should return false when date is outside range")
    void shouldReturnFalseWhenDateIsOutsideRange() {
        DateRange range = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 6, 30, 0, 0)
        );
        LocalDateTime testDate = LocalDateTime.of(2024, 12, 1, 0, 0);

        assertThat(range.includes(testDate)).isFalse();
    }

    @Test
    @DisplayName("should detect overlapping date ranges")
    void shouldDetectOverlappingRanges() {
        DateRange range1 = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 6, 30, 0, 0)
        );
        DateRange range2 = new DateRange(
                LocalDateTime.of(2024, 6, 1, 0, 0),
                LocalDateTime.of(2024, 12, 31, 0, 0)
        );

        assertThat(range1.overlaps(range2)).isTrue();
    }

    @Test
    @DisplayName("should detect non-overlapping date ranges")
    void shouldDetectNonOverlappingRanges() {
        DateRange range1 = new DateRange(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                LocalDateTime.of(2024, 3, 31, 0, 0)
        );
        DateRange range2 = new DateRange(
                LocalDateTime.of(2024, 4, 1, 0, 0),
                LocalDateTime.of(2024, 6, 30, 0, 0)
        );

        assertThat(range1.overlaps(range2)).isFalse();
    }
}
