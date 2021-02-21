package com.jacoblucas.peloton.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Value.Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableUserData.class)
public abstract class UserData {
    @JsonProperty("id")
    public abstract String getId();

    @JsonProperty("username")
    public abstract String getUsername();

    @JsonProperty("name")
    public abstract String getName();

    @JsonProperty("gender")
    public abstract String getGender();

    @JsonProperty("location")
    public abstract Optional<String> getLocation();

    @Value.Auxiliary
    @JsonProperty("default_heart_rate_zones")
    public abstract List<Double> getDefaultHeartRateZones();

    @Value.Auxiliary
    @JsonProperty("customized_heart_rate_zones")
    public abstract List<Double> getCustomizedHeartRateZones();

    @Value.Derived
    public List<Double> getHeartRateZones() {
        if (!getCustomizedHeartRateZones().isEmpty()) {
            return getCustomizedHeartRateZones();
        } else if (!getDefaultHeartRateZones().isEmpty()) {
            return getDefaultHeartRateZones();
        } else {
            return Collections.emptyList();
        }
    }

    @Value.Auxiliary
    @JsonProperty("default_max_heart_rate")
    public abstract int getDefaultMaxHeartRate();

    @Value.Auxiliary
    @JsonProperty("customized_max_heart_rate")
    public abstract int getCustomizedMaxHeartRate();

    @Value.Derived
    public int getMaxHeartRate() {
        if (getCustomizedMaxHeartRate() > 0) {
            return getCustomizedMaxHeartRate();
        } else if (getDefaultMaxHeartRate() > 0) {
            return getDefaultMaxHeartRate();
        } else {
            return 0;
        }
    }

    @Value.Auxiliary
    @JsonProperty("workout_counts")
    public abstract List<WorkoutCount> getWorkoutCounts();

    @Value.Derived
    public Map<String, Integer> getWorkoutCountsByFitnessDiscipline() {
        return getWorkoutCounts().stream()
                .filter(wc -> wc.getCount() > 0)
                .collect(Collectors.toMap(WorkoutCount::getName, WorkoutCount::getCount));
    }
}
