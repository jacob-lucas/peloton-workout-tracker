package com.jacoblucas.peloton.utils;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Pattern;

public final class TimestampParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm (z)");
    private static final DateTimeFormatter ALT_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm (x)");

    public static Instant parse(final String str) {
        return Instant.from(getDateTime(str));
    }

    private static TemporalAccessor getDateTime(final String str) {
        final Pattern pattern = Pattern.compile(".*[A-Z]{3}.*");
        final DateTimeFormatter dateTimeFormatter;
        if (pattern.matcher(str).matches()) {
            dateTimeFormatter = DATE_TIME_FORMATTER;
        } else {
            dateTimeFormatter = ALT_DATE_TIME_FORMATTER;
        }
        return dateTimeFormatter.parse(str);
    }
}
