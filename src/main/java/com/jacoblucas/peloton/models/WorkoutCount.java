package com.jacoblucas.peloton.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(as = ImmutableWorkoutCount.class)
public abstract class WorkoutCount {
    @JsonProperty("name")
    public abstract String getName();

    @Value.Auxiliary
    @JsonProperty("slug")
    public abstract String getSlug();

    @JsonProperty("count")
    public abstract int getCount();

    @Value.Auxiliary
    @JsonProperty("icon_url")
    public abstract String getIconUrl();
}
