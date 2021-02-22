package com.jacoblucas.peloton.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jacoblucas.peloton.models.Bucket;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class WorkoutSummaryRequestTest {
    private static ObjectMapper OBJECT_MAPPER;

    @BeforeClass
    public static void setUpSuite() {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.registerModule(new GuavaModule());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimezone() {
        ImmutableWorkoutSummaryRequest.builder()
                .requestContext(ImmutableRequestContext.of("a", "1"))
                .bucket(Bucket.WEEKLY)
                .timezoneString("ABCDE")
                .build();
    }

    @Test
    public void testDeserialize() throws IOException {
        final String json = String.join("", Files.readAllLines(Paths.get("src/test/resources/WorkoutSummaryRequestExample.json"), StandardCharsets.UTF_8));

        final WorkoutSummaryRequest request = OBJECT_MAPPER.readValue(json, WorkoutSummaryRequest.class);

        final WorkoutSummaryRequest expected = ImmutableWorkoutSummaryRequest.builder()
                .requestContext(ImmutableRequestContext.of("foo@gmail.com", "abcdefg"))
                .bucket(Bucket.WEEKLY)
                .timezoneString("America/Los_Angeles")
                .fromDate(LocalDate.of(2021,1,1))
                .build();

        assertThat(request, is(expected));
    }
}
