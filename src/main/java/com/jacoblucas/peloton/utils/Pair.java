package com.jacoblucas.peloton.utils;

import org.immutables.value.Value;

@Value.Immutable
@Value.Style(allParameters = true)
public abstract class Pair<T, U> {
    public abstract T getFirst();

    public abstract U getSecond();
}
