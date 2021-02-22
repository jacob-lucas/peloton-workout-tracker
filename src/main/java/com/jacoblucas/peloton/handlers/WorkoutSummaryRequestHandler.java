package com.jacoblucas.peloton.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.jacoblucas.peloton.api.PelotonApiAdapter;
import com.jacoblucas.peloton.api.PelotonApiAdapterFactory;
import com.jacoblucas.peloton.models.UserAuth;
import com.jacoblucas.peloton.models.Workout;
import com.jacoblucas.peloton.utils.WorkoutSummarizer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class WorkoutSummaryRequestHandler implements RequestHandler<WorkoutSummaryRequest, WorkoutSummaryResponse> {
    @Override
    public WorkoutSummaryResponse handleRequest(final WorkoutSummaryRequest input, final Context context) {
        final PelotonApiAdapter adapter = PelotonApiAdapterFactory.instance();
        try {
            final UserAuth userAuth = adapter.login(input.getRequestContext().getUsername(), input.getRequestContext().getPassword());
            context.getLogger().log(String.format("Logged in with: %s", userAuth));

            final List<Workout> history = adapter.getWorkoutHistory(userAuth, input.getFromDate(), input.getToDate());

            final Map<LocalDate, List<Workout>> summary = WorkoutSummarizer.summarize(history, input.getBucket(), input.getTimezone());

            return ImmutableWorkoutSummaryResponse.builder()
                    .fromDate(input.getFromDate())
                    .bucket(input.getBucket())
                    .workoutSummary(summary)
                    .build();
        } catch (IOException e) {
            context.getLogger().log(String.format("Unable to process %s: %s", input, e));
            return null;
        }
    }
}
