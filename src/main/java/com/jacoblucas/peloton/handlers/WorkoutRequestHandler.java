package com.jacoblucas.peloton.handlers;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.jacoblucas.peloton.api.PelotonApiAdapter;
import com.jacoblucas.peloton.api.PelotonApiAdapterFactory;
import com.jacoblucas.peloton.models.UserAuth;
import com.jacoblucas.peloton.models.Workout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class WorkoutRequestHandler extends RequestHandler implements RequestStreamHandler {
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        final PelotonApiAdapter adapter = PelotonApiAdapterFactory.instance();
        try {
            final WorkoutRequest request = objectMapper.readValue(input, WorkoutRequest.class);
            final UserAuth userAuth = adapter.login(request.getRequestContext().getUsername(), request.getRequestContext().getPassword());
            context.getLogger().log(String.format("Logged in with: %s", userAuth));

            final List<Workout> history = adapter.getWorkoutHistory(userAuth, request.getFromDate(), request.getToDate());

            final WorkoutResponse response = ImmutableWorkoutResponse.builder()
                    .fromDate(request.getFromDate())
                    .toDate(request.getToDate())
                    .workouts(history)
                    .build();

            objectMapper.writeValue(output, response);
        } catch (final IOException e) {
            final String msg = String.format("Unable to process %s: %s", input, e);
            context.getLogger().log(msg);
            throw e;
        }
    }
}
