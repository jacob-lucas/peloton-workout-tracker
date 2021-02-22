package com.jacoblucas.peloton.utils;

import com.google.common.collect.ImmutableList;
import com.jacoblucas.peloton.handlers.Bucket;
import com.jacoblucas.peloton.models.Workout;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutSummarizerTest {
    @Test
    public void testSummarizeDaily() {
        final List<Workout> history = ImmutableList.of(
                Workout.parse("2020-09-14 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-27 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-01 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-02 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get());

        final Map<LocalDate, List<Workout>> summary = WorkoutSummarizer.summarize(history, Bucket.DAILY);
        assertThat(summary.get(LocalDate.of(2020,9,14)).size(), is(1));
        assertThat(summary.get(LocalDate.of(2020,9,24)).size(), is(1));
        assertThat(summary.get(LocalDate.of(2020,9,25)).size(), is(2));
        assertThat(summary.get(LocalDate.of(2020,9,27)).size(), is(1));
        assertThat(summary.get(LocalDate.of(2020,10, 1)).size(), is(1));
        assertThat(summary.get(LocalDate.of(2020,10, 2)).size(), is(1));
    }

    @Test
    public void testSummarizeWeekly() {
        final List<Workout> history = ImmutableList.of(
                Workout.parse("2020-09-14 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-27 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-01 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-02 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get());

        final Map<LocalDate, List<Workout>> summary = WorkoutSummarizer.summarize(history, Bucket.WEEKLY);
        assertThat(summary.get(LocalDate.of(2020,9,14)).size(), is(1));
        assertThat(summary.get(LocalDate.of(2020,9,21)).size(), is(4));
        assertThat(summary.get(LocalDate.of(2020,9,28)).size(), is(2));
    }

    @Test
    public void testSummarizeMonthly() {
        final List<Workout> history = ImmutableList.of(
                Workout.parse("2020-09-14 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-24 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-25 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-09-27 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-01 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get(),
                Workout.parse("2020-10-02 08:13 (PDT),On Demand,Denis Morton,30,Cycling,Power Zone,30 min Power Zone Ride,2020-09-15 04:23 (PDT),269,150,43%,85,18.45,9.22,534,152.57,,").get());

        final Map<LocalDate, List<Workout>> summary = WorkoutSummarizer.summarize(history, Bucket.MONTHLY);
        assertThat(summary.get(LocalDate.of(2020,9,1)).size(), is(5));
        assertThat(summary.get(LocalDate.of(2020,10,1)).size(), is(2));
    }
}
