package com.jacoblucas.peloton.handlers;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
@JsonDeserialize(as = ImmutableRequestContext.class)
public abstract class RequestContext {
    public abstract String getUsername();

    public abstract String getPassword();
}
