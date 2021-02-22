package com.jacoblucas.peloton.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.jacoblucas.peloton.api.PelotonApiAdapter;
import com.jacoblucas.peloton.api.PelotonApiAdapterFactory;
import com.jacoblucas.peloton.models.UserAuth;
import com.jacoblucas.peloton.models.Workout;
import com.jacoblucas.peloton.utils.WorkoutSummarizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class WorkoutSummaryRequestHandler extends RequestHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(final InputStream input, final OutputStream output, final Context context) throws IOException {
        final PelotonApiAdapter adapter = PelotonApiAdapterFactory.instance();
        try {
            final WorkoutSummaryRequest request = objectMapper.readValue(input, WorkoutSummaryRequest.class);
            final UserAuth userAuth = adapter.login(request.getRequestContext().getUsername(), request.getRequestContext().getPassword());
            context.getLogger().log(String.format("Logged in with: %s", userAuth));

            final List<Workout> history = adapter.getWorkoutHistory(userAuth, request.getFromDate(), request.getToDate());

            final Map<LocalDate, List<Workout>> summary = WorkoutSummarizer.summarize(history, request.getBucket(), request.getTimezone());

            final WorkoutSummaryResponse response = ImmutableWorkoutSummaryResponse.builder()
                    .fromDate(request.getFromDate())
                    .toDate(request.getToDate())
                    .bucket(request.getBucket())
                    .workoutSummary(summary)
                    .build();

            objectMapper.writeValue(output, response);
        } catch (final Exception e) {
            final String msg = String.format("Unable to process %s: %s", input, e);
            context.getLogger().log(msg);
        }
    }
}
