package com.jacoblucas.peloton.handlers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.jacoblucas.peloton.models.Bucket;
import org.immutables.value.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.zone.ZoneRulesException;

@Value.Immutable
@JsonDeserialize(as = ImmutableWorkoutSummaryRequest.class)
public abstract class WorkoutSummaryRequest {
    public abstract RequestContext getRequestContext();

    public abstract Bucket getBucket();

    public abstract String getTimezoneString();

    @Value.Default
    public LocalDate getFromDate() {
        return LocalDate.MIN;
    }

    @Value.Default
    public LocalDate getToDate() {
        return LocalDate.MAX;
    }

    @JsonIgnore
    public ZoneId getTimezone() {
        return ZoneId.of(getTimezoneString());
    }

    @Value.Check
    public void check() {
        try {
            getTimezone();
        } catch (final ZoneRulesException zre) {
            throw new IllegalArgumentException(zre.getMessage());
        }
    }
}
