package com.jacoblucas.peloton.utils;

import com.google.common.collect.ImmutableMap;
import com.jacoblucas.peloton.models.Bucket;
import com.jacoblucas.peloton.models.Workout;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class WorkoutSummarizer {
    private static final Map<Bucket, TemporalAdjuster> ADJUSTORS = ImmutableMap.of(
            Bucket.DAILY, TemporalAdjusters.ofDateAdjuster(d -> d),
            Bucket.WEEKLY, TemporalAdjusters.previousOrSame(DayOfWeek.of(1)),
            Bucket.MONTHLY, TemporalAdjusters.firstDayOfMonth());

    private WorkoutSummarizer() {}

    public static Map<LocalDate, List<Workout>> summarize(
            final List<Workout> history,
            final Bucket bucket,
            final ZoneId timezone
    ) {
        return history.stream()
                .collect(Collectors.groupingBy(w ->
                        LocalDateTime.ofInstant(w.getTimestamp(), timezone)
                                .toLocalDate()
                                .with(ADJUSTORS.get(bucket))));
    }
}
