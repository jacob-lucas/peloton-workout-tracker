package com.jacoblucas.peloton.utils;

import io.vavr.control.Try;

import java.util.Optional;
import java.util.function.Function;

public final class ArrayHelper {

    public static <T, U> Optional<U> parseAt(final T[] arr, final int idx, final Function<T, U> parseFunc) {
        return Optional.ofNullable(Try.ofCallable(() -> parseFunc.apply(arr[idx])).getOrElse(() -> null));
    }

    private ArrayHelper() {}
}
