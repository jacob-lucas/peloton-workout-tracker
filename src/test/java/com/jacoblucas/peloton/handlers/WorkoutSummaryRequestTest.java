package com.jacoblucas.peloton.handlers;

import com.jacoblucas.peloton.models.Bucket;
import org.junit.Test;

public class WorkoutSummaryRequestTest {
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTimezone() {
        ImmutableWorkoutSummaryRequest.builder()
                .requestContext(ImmutableRequestContext.of("a", "1"))
                .bucket(Bucket.WEEKLY)
                .timezoneString("ABCDE")
                .build();
    }
}
