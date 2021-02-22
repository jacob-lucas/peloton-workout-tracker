package com.jacoblucas.peloton.handlers;

import com.jacoblucas.peloton.models.Bucket;
import com.jacoblucas.peloton.models.Workout;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Value.Immutable
public abstract class WorkoutSummaryResponse {
    public abstract Bucket getBucket();

    public abstract LocalDate getFromDate();

    @Value.Derived
    public LocalDate getToDate() {
        final Bucket bucket = getBucket();
        switch (bucket) {
            case DAILY:
                return getFromDate().plusDays(1);
            case WEEKLY:
                return getFromDate().plusWeeks(1);
            case MONTHLY:
                return getFromDate().plusMonths(1);
            default:
                throw new IllegalArgumentException("Unsupported bucket: " + bucket.name());
        }
    }

    public abstract Map<LocalDate, List<Workout>> getWorkoutSummary();
}
