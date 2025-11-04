package com.luissoy.historicalprices.domain.shared.valueobject;

import com.luissoy.historicalprices.domain.shared.exception.ValidationException;

import java.time.LocalDateTime;

public record DateRange(LocalDateTime start, LocalDateTime end) {

    public DateRange {
        if (start == null)
            throw new ValidationException("Start dates cannot be null");
        if (end != null && end.isBefore(start))
            throw new ValidationException("End date must be after start date");
    }

    public boolean includes(LocalDateTime date) {
        if (date == null) return false;
        if (end == null) return !date.isBefore(start);
        return !date.isBefore(start) && !date.isAfter(end);
    }

    public boolean overlaps(DateRange other) {
        if (other == null) return false;

        LocalDateTime thisEnd = this.end == null ? LocalDateTime.MAX : this.end;
        LocalDateTime otherEnd = other.end == null ? LocalDateTime.MAX : other.end;

        return this.start.isBefore(otherEnd) && other.start.isBefore(thisEnd);
    }
}