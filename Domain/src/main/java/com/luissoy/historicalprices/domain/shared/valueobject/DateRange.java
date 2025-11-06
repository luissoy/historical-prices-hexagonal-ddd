package com.luissoy.historicalprices.domain.shared.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

import java.time.LocalDate;

public record DateRange(LocalDate start, LocalDate end) {

    public DateRange {
        if (start == null)
            throw new ValidationException("Start dates cannot be null");
        if (end != null && end.isBefore(start))
            throw new ValidationException("End date must be after start date");
    }

    public boolean includes(LocalDate date) {
        if (date == null) return false;
        if (end == null) return !date.isBefore(start);
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public boolean overlaps(DateRange other) {
        if (other == null) return false;

        LocalDate thisEnd = this.end == null ? LocalDate.MAX : this.end;
        LocalDate otherEnd = other.end == null ? LocalDate.MAX : other.end;

        return !(thisEnd.isBefore(other.start) || otherEnd.isBefore(this.start));
    }
}