package com.jacoblucas.peloton.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableUserAuth.class)
public abstract class UserAuth {
    @JsonProperty("session_id")
    public abstract String getSessionId();

    @JsonProperty("user_id")
    public abstract String getUserId();
}
