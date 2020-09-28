package com.jacoblucas.peloton.workout.models;

import io.vavr.control.Try;
import org.immutables.value.Value;

import java.time.Instant;

@Value.Immutable
public abstract class Workout {
    public abstract Instant getTimestamp();

    public abstract boolean isLive();

    public abstract String getInstructorName();

    public abstract int getMinutes();

    public abstract String getFitnessDiscipline();

    public abstract String getType();

    public abstract Instant getClassTimestamp();

    public abstract int getOutput();

    public abstract int getAverageOutput();

    public abstract double getAverageResistance();

    public abstract int getAverageCadence();

    public abstract double getAverageSpeed();

    public abstract String getAverageSpeedUnits();

    public abstract double getDistance();

    public abstract String getDistanceUnits();

    public abstract int getCaloriesBurned();

    public abstract double getAverageHeartRate();

    public abstract int getAverageIncline();

    public abstract double getAveragePace();

    public abstract double getAveragePaceUnits();

    public static Try<Workout> parse(final String csv) {
        return Try.failure(new RuntimeException("not yet implemented"));
    }
}
