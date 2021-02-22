package com.jacoblucas.peloton.handlers;

import com.jacoblucas.peloton.models.Bucket;
import org.junit.Test;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutSummaryResponseTest {
    @Test
    public void testGetToDateForDaily() {
        final WorkoutSummaryResponse response = ImmutableWorkoutSummaryResponse.builder()
                .bucket(Bucket.DAILY)
                .fromDate(LocalDate.of(2020, 7, 29))
                .build();

        assertThat(response.getToDate(), is(LocalDate.of(2020,7,30)));
    }

    @Test
    public void testGetToDateForWeekly() {
        final WorkoutSummaryResponse response = ImmutableWorkoutSummaryResponse.builder()
                .bucket(Bucket.WEEKLY)
                .fromDate(LocalDate.of(2020, 7, 29))
                .build();

        assertThat(response.getToDate(), is(LocalDate.of(2020,8,5)));
    }

    @Test
    public void testGetToDateForMonthly() {
        final WorkoutSummaryResponse response = ImmutableWorkoutSummaryResponse.builder()
                .bucket(Bucket.MONTHLY)
                .fromDate(LocalDate.of(2020, 7, 29))
                .build();

        assertThat(response.getToDate(), is(LocalDate.of(2020, 8, 29)));
    }
}
