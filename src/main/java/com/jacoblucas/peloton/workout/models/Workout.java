package com.jacoblucas.peloton.workout.models;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import io.vavr.control.Try;
import org.immutables.value.Value;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Value.Immutable
public abstract class Workout {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm (z)");

    private static final Set<FitnessDiscipline> SUPPORTED_FITNESS_DISCIPLINES = ImmutableSet.of(FitnessDiscipline.CYCLING);
    private static final String DELIMITER = ",";

    public abstract Instant getTimestamp();

    public abstract boolean isLive();

    public abstract String getInstructorName();

    public abstract int getMinutes();

    public abstract FitnessDiscipline getFitnessDiscipline();

    public abstract String getType();

    public abstract String getTitle();

    public abstract Instant getClassTimestamp();

    public abstract int getOutput();

    public abstract int getAverageOutput();

    public abstract double getAverageResistance();

    public abstract int getAverageCadence();

    public abstract double getAverageSpeed();

    public abstract double getDistance();

    public abstract int getCaloriesBurned();

    public abstract double getAverageHeartRate();

    @Value.Check
    public void check() {
        Preconditions.checkArgument(SUPPORTED_FITNESS_DISCIPLINES.contains(getFitnessDiscipline()), String.format("Provided fitness discipline [%s] is not supported", getFitnessDiscipline().name()));
    }

    /**
     * Parse a CSV entry from the peloton workout export file.
     * Example:
     *  2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,,,,534,152.57,,
     * @param csv the CSV line to parse (see the above example).
     * @return A workout object containing all the data from the provided CSV line.
     */
    public static Try<Workout> parse(final String csv) {
        return Try.ofCallable(() -> {
            final String[] parts = csv.split(DELIMITER);

            final Instant timestamp = Instant.from(DATE_TIME_FORMATTER.parse(parts[0]));
            final Instant classTimestamp = Instant.from(DATE_TIME_FORMATTER.parse(parts[7]));

            return ImmutableWorkout.builder()
                    .timestamp(timestamp)
                    .isLive(parts[1].equalsIgnoreCase("live"))
                    .instructorName(parts[2])
                    .minutes(Integer.parseInt(parts[3]))
                    .fitnessDiscipline(FitnessDiscipline.valueOf(parts[4].toUpperCase()))
                    .type(parts[5])
                    .title(parts[6])
                    .classTimestamp(classTimestamp)
                    .output(Integer.parseInt(parts[8]))
                    .averageOutput(Integer.parseInt(parts[9]))
                    .averageResistance(Double.parseDouble(parts[10].replaceAll("%", "")) / 100)
                    .averageCadence(Integer.parseInt(parts[11]))
                    .averageSpeed(Double.parseDouble(parts[12]))
                    .distance(Double.parseDouble(parts[13]))
                    .caloriesBurned(Integer.parseInt(parts[14]))
                    .averageHeartRate(Double.parseDouble(parts[15]))
                    .build();
        });
    }
}
