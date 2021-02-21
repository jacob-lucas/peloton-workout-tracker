package com.jacoblucas.peloton.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = ImmutableLoginRequest.class)
public abstract class LoginRequest {
    @JsonProperty("username_or_email")
    public abstract String getUsername();

    @JsonProperty("password")
    public abstract String getPassword();
}
