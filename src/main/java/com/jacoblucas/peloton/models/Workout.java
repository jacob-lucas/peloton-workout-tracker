package com.jacoblucas.peloton.models;

import com.google.common.collect.ImmutableSet;
import com.jacoblucas.peloton.utils.ArrayHelper;
import io.vavr.control.Try;
import org.immutables.value.Value;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Value.Immutable
public abstract class Workout {
    static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm (z)");
    static final DateTimeFormatter ALT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm (x)");

    private static final String DELIMITER = ",";
    private static final Set<String> SUPPORTED_FITNESS_DISCIPLINES = ImmutableSet.of("Cycling");

    public abstract Instant getTimestamp();

    public abstract boolean isLive();

    public abstract String getInstructorName();

    public abstract int getMinutes();

    public abstract String getFitnessDiscipline();

    public abstract String getType();

    public abstract String getTitle();

    public abstract Instant getClassTimestamp();

    public abstract int getTotalOutput();

    public abstract int getAverageWatts();

    public abstract int getAverageResistance();

    public abstract int getAverageCadence();

    public abstract double getAverageSpeed();

    public abstract double getDistance();

    public abstract int getCaloriesBurned();

    public abstract Optional<Double> getAverageHeartRate();

    public abstract Optional<Double> getAverageIncline();

    public abstract Optional<Double> getAveragePace();

    @Value.Derived
    public Optional<Double> getOutputPerMinute() {
        final int output = getTotalOutput();
        final int minutes = getMinutes();

        if (minutes == 0) {
            return Optional.empty();
        } else {
            return Optional.of((double) output / minutes);
        }
    }

    /**
     * Parse a CSV entry from the com.jacoblucas.peloton workout export file.
     * Example:
     *  2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,,,,534,152.57,,
     * @param csv the CSV line to parse (see the above example).
     * @return A workout object containing all the data from the provided CSV line.
     */
    public static Try<Workout> parse(final String csv) {
        return Try.ofCallable(() -> {
            try {
                final String[] parts = csv.split(DELIMITER);

                final Instant timestamp = Instant.from(getDateTime(parts[0]));
                final Instant classTimestamp = Instant.from(getDateTime(parts[7]));

                final String fitnessDiscipline = parts[4];
                if (!SUPPORTED_FITNESS_DISCIPLINES.contains(fitnessDiscipline)) {
                    return null;
                }

                return ImmutableWorkout.builder()
                        .timestamp(timestamp)
                        .isLive(parts[1].equalsIgnoreCase("live"))
                        .instructorName(parts[2])
                        .minutes(Integer.parseInt(parts[3]))
                        .fitnessDiscipline(fitnessDiscipline)
                        .type(parts[5])
                        .title(parts[6])
                        .classTimestamp(classTimestamp)
                        .totalOutput(Integer.parseInt(parts[8]))
                        .averageWatts(Integer.parseInt(parts[9]))
                        .averageResistance(Integer.parseInt(parts[10].replaceAll("%", "")))
                        .averageCadence(Integer.parseInt(parts[11]))
                        .averageSpeed(Double.parseDouble(parts[12]))
                        .distance(Double.parseDouble(parts[13]))
                        .caloriesBurned(Integer.parseInt(parts[14]))
                        .averageHeartRate(ArrayHelper.parseAt(parts, 15, Double::parseDouble))
                        .averageIncline(ArrayHelper.parseAt(parts, 16, Double::parseDouble))
                        .averagePace(ArrayHelper.parseAt(parts, 17, Double::parseDouble))
                        .build();
            } catch (Throwable t) {
                System.out.println("Failed to parse: " + csv);
                System.out.println(t.toString());
                throw t;
            }
        });
    }

    private static TemporalAccessor getDateTime(final String str) {
        final Pattern pattern = Pattern.compile(".*[A-Z]{3}.*");
        final DateTimeFormatter dateTimeFormatter;
        if (pattern.matcher(str).matches()) {
            dateTimeFormatter = DATE_TIME_FORMATTER;
        } else {
            dateTimeFormatter = ALT_DATE_TIME_FORMATTER;
        }
        return dateTimeFormatter.parse(str);
    }
}
