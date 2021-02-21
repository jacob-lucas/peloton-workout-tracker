package com.jacoblucas.peloton.utils;

import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ArrayHelperTest {
    @Test
    public void testParseAtValid() {
        final String[] arr = {"1.0", "2.3", "abc"};
        final Optional<Double> result = ArrayHelper.parseAt(arr, 1, Double::parseDouble);

        assertThat(result.isPresent(), is(true));
        assertThat(result.get(), is(2.3D));
    }

    @Test
    public void testParseAtInvalid() {
        final String[] arr = {"1.0", "2.3", "abc"};
        final Optional<Double> result = ArrayHelper.parseAt(arr, 2, Double::parseDouble);

        assertThat(result.isPresent(), is(false));
    }

    @Test
    public void testParseAtInvalidIndex() {
        final String[] arr = {"1.0", "2.3", "abc"};
        final Optional<Double> result = ArrayHelper.parseAt(arr, 99, Double::parseDouble);

        assertThat(result.isPresent(), is(false));
    }
}
