package com.jacoblucas.peloton.handlers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jacoblucas.peloton.models.Workout;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.util.List;

@Value.Immutable
@JsonDeserialize(as = ImmutableWorkoutResponse.class)
public abstract class WorkoutResponse {
    public abstract LocalDate getFromDate();

    public abstract LocalDate getToDate();

    public abstract List<Workout> getWorkouts();
}
