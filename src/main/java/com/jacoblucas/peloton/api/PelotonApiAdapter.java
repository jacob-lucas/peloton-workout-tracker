package com.jacoblucas.peloton.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.jacoblucas.peloton.io.HttpClient;
import com.jacoblucas.peloton.models.ImmutableLoginRequest;
import com.jacoblucas.peloton.models.UserAuth;
import com.jacoblucas.peloton.models.UserData;
import com.jacoblucas.peloton.models.Workout;
import io.vavr.control.Try;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PelotonApiAdapter {
    static final String LOGIN_URL = "https://api.onepeloton.com/auth/login";
    static final String USER_DATA_URL = "https://api.onepeloton.com/api/user/%s";
    static final String WORKOUT_HISTORY_URL = "https://api.onepeloton.com/api/user/%s/workout_history_csv";
    static final String SESSION_ID = "peloton_session_id";

    private final HttpClient httpClient;

    public PelotonApiAdapter(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public UserAuth login(final String username, final String password) throws IOException {
        return httpClient.post(
                LOGIN_URL,
                ImmutableLoginRequest.of(username, password),
                new TypeReference<UserAuth>(){});
    }

    public UserData getUserData(final UserAuth userAuth) throws IOException {
        return httpClient.get(
                String.format(USER_DATA_URL, userAuth.getUserId()),
                ImmutableMap.of(SESSION_ID, userAuth.getSessionId()),
                new TypeReference<UserData>() {});
    }

    public List<Workout> getWorkoutHistory(final UserAuth userAuth) throws IOException {
        final String url = String.format(WORKOUT_HISTORY_URL, userAuth.getUserId());
        final String csv = httpClient.get(url, ImmutableMap.of(SESSION_ID, userAuth.getSessionId()));

        final String[] entries = csv.split("\n");

        return Arrays.stream(entries)
                .map(Workout::parse)
                .map(Try::getOrNull)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Workout> getWorkoutHistory(final UserAuth userAuth, final LocalDate fromDate, final LocalDate toDate) throws IOException {
        return getWorkoutHistory(userAuth).stream()
                .filter(w -> Date.from(w.getTimestamp()).after(Date.valueOf(fromDate)))
                .filter(w -> Date.from(w.getTimestamp()).before(Date.valueOf(toDate)))
                .collect(Collectors.toList());
    }
}
