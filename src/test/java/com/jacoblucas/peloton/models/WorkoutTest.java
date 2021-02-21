package com.jacoblucas.peloton.models;

import io.vavr.control.Try;
import org.junit.Test;

import java.time.Instant;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutTest {
    private static final String EXAMPLE = "2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,";
    private static final String EXAMPLE_TZ = "2021-02-01 18:23 (-08),On Demand,Kendall Toole,30,Cycling,Theme,30 min Movie Buff Ride,2021-01-25 08:25 (-08),271,151,45%,80,18.75,9.37,526,150.69,,";
    private static final String EXAMPLE_NO_HR = "2020-04-17 12:29 (PDT),On Demand,Cody Rigsby,20,Cycling,Theme,20 min Beginner Ride,2018-08-24 12:11 (PDT),119,99,37%,84,15.81,5.26,164,,,";

    @Test
    public void testSuccessfulParse() {
        final Try<Workout> workoutTry = Workout.parse(EXAMPLE);

        final Workout expected = ImmutableWorkout.builder()
                .timestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2020-09-24 08:13 (PDT)")))
                .isLive(false)
                .instructorName("Denis Morton")
                .minutes(30)
                .fitnessDiscipline("Cycling")
                .type("Power Zone")
                .title("30 min Power Zone Ride")
                .classTimestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2020-09-15 04:23 (PDT)")))
                .totalOutput(269)
                .averageWatts(150)
                .averageResistance(43)
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
    public void testSuccessfulParseAlternateTimezoneFormat() {
        final Try<Workout> workoutTry = Workout.parse(EXAMPLE_TZ);

        final Workout expected = ImmutableWorkout.builder()
                .timestamp(Instant.from(Workout.ALT_DATE_TIME_FORMATTER.parse("2021-02-01 18:23 (-08)")))
                .isLive(false)
                .instructorName("Kendall Toole")
                .minutes(30)
                .fitnessDiscipline("Cycling")
                .type("Theme")
                .title("30 min Movie Buff Ride")
                .classTimestamp(Instant.from(Workout.ALT_DATE_TIME_FORMATTER.parse("2021-01-25 08:25 (-08)")))
                .totalOutput(271)
                .averageWatts(151)
                .averageResistance(45)
                .averageCadence(80)
                .averageSpeed(18.75)
                .distance(9.37)
                .caloriesBurned(526)
                .averageHeartRate(150.69)
                .build();

        assertThat(workoutTry.isSuccess(), is(true));
        final Workout actual = workoutTry.get();
        assertThat(actual, is(expected));
        System.out.println(actual);
    }

    @Test
    public void testSuccessfulParseNoHeartRateData() {
        final Try<Workout> workoutTry = Workout.parse(EXAMPLE_NO_HR);

        final Workout expected = ImmutableWorkout.builder()
                .timestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2020-04-17 12:29 (PDT)")))
                .isLive(false)
                .instructorName("Cody Rigsby")
                .minutes(20)
                .fitnessDiscipline("Cycling")
                .type("Theme")
                .title("20 min Beginner Ride")
                .classTimestamp(Instant.from(Workout.DATE_TIME_FORMATTER.parse("2018-08-24 12:11 (PDT)")))
                .totalOutput(119)
                .averageWatts(99)
                .averageResistance(37)
                .averageCadence(84)
                .averageSpeed(15.81)
                .distance(5.26)
                .caloriesBurned(164)
                .build();

        if (workoutTry.isFailure()) System.out.println(workoutTry.getCause().toString());
        assertThat(workoutTry.isSuccess(), is(true));
        final Workout actual = workoutTry.get();
        assertThat(actual, is(expected));
        System.out.println(actual);
    }

    @Test
    public void testGetOutputPerMinuteZeroMinutes() {
        final Workout workout = ImmutableWorkout.copyOf(Workout.parse(EXAMPLE).get())
                .withMinutes(0);

        assertThat(workout.getOutputPerMinute().isPresent(), is(false));
    }

    @Test
    public void testGetOutputPerMinuteZeroOutput() {
        final Workout workout = ImmutableWorkout.copyOf(Workout.parse(EXAMPLE).get())
                .withTotalOutput(0);

        assertThat(workout.getOutputPerMinute().get(), is(0.0D));
    }

    @Test
    public void testGetOutputPerMinute() {
        final Workout workout = Workout.parse(EXAMPLE).get();

        assertThat(workout.getOutputPerMinute().get(), is(8.966666666666667D));
    }
}
