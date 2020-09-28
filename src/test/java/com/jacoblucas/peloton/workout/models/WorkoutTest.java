package com.jacoblucas.peloton.workout.models;

import io.vavr.control.Try;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutTest {
    private static final String EXAMPLE = "2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,";

    @Test
    public void testSuccessfulParse() {
        final Try<Workout> workoutTry = Workout.parse(EXAMPLE);

        final Workout expected = ImmutableWorkout.builder()
                .timestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2020-09-24 08:13 (PDT)")))
                .isLive(false)
                .instructorName("Denis Morton")
                .minutes(30)
                .fitnessDiscipline(FitnessDiscipline.CYCLING)
                .type("Power Zone")
                .title("30 min Power Zone Ride")
                .classTimestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2020-09-15 04:23 (PDT)")))
                .output(269)
                .averageOutput(150)
                .averageResistance(0.43)
                .averageCadence(85)
                .averageSpeed(18.45)
                .distance(9.22)
                .caloriesBurned(534)
                .averageHeartRate(152.57)
                .build();

        if (workoutTry.isFailure()) System.out.println(workoutTry.getCause().toString());
        assertThat(workoutTry.isSuccess(), is(true));
        final Workout actual = workoutTry.get();
        assertThat(actual, is(expected));
        System.out.println(actual);
    }

    @Test
    public void testUnsupportedFitnessDiscipline() {
        final String unsupportedCsv = EXAMPLE.replaceAll("Cycling", "Running");
        final Try<Workout> workoutTry = Workout.parse(unsupportedCsv);
        assertThat(workoutTry.isFailure(), is(true));
    }
}
