package com.jacoblucas.peloton.handlers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jacoblucas.peloton.models.Bucket;
import com.jacoblucas.peloton.models.Workout;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Value.Immutable
@JsonDeserialize(as = ImmutableWorkoutSummaryResponse.class)
public abstract class WorkoutSummaryResponse {
    public abstract Bucket getBucket();

    public abstract LocalDate getFromDate();

    public abstract LocalDate getToDate();

    public abstract Map<LocalDate, List<Workout>> getWorkoutSummary();
}
