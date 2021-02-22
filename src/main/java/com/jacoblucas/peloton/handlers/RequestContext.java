package com.jacoblucas.peloton.handlers;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
public abstract class RequestContext {
    public abstract String getUsername();

    public abstract String getPassword();
}
