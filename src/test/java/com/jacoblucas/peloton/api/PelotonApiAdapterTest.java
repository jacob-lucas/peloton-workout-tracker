package com.jacoblucas.peloton.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.ImmutableMap;
import com.jacoblucas.peloton.io.HttpClient;
import com.jacoblucas.peloton.models.ImmutableUserAuth;
import com.jacoblucas.peloton.models.LoginRequest;
import com.jacoblucas.peloton.models.UserAuth;
import com.jacoblucas.peloton.models.Workout;
import org.apache.http.client.HttpResponseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.jacoblucas.peloton.api.PelotonApiAdapter.LOGIN_URL;
import static com.jacoblucas.peloton.api.PelotonApiAdapter.SESSION_ID;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PelotonApiAdapterTest {

    @Mock private HttpClient mockHttpClient;

    private PelotonApiAdapter api;

    @Before
    public void setUp() {
        api = new PelotonApiAdapter(mockHttpClient);
    }

    @Test
    public void testLogin() throws IOException {
        final UserAuth userAuth = ImmutableUserAuth.builder()
                .sessionId("abc")
                .userId("123")
                .build();

        final ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<LoginRequest> requestCaptor = ArgumentCaptor.forClass(LoginRequest.class);
        when(mockHttpClient.post(urlCaptor.capture(), requestCaptor.capture(), any(TypeReference.class))).thenReturn(userAuth);

        final UserAuth response = api.login("foo", "bar");

        assertThat(urlCaptor.getValue(), is(LOGIN_URL));
        assertThat(requestCaptor.getValue().getUsername(), is("foo"));
        assertThat(requestCaptor.getValue().getPassword(), is("bar"));

        assertThat(response.getSessionId(), is("abc"));
        assertThat(response.getUserId(), is("123"));
    }

    @Test(expected = IOException.class)
    public void testGetWorkoutHistoryNotLoggedIn() throws IOException {
        final String userId = "foo";
        final String invalidSessionId = "abc123";
        when(mockHttpClient.get(String.format(PelotonApiAdapter.WORKOUT_HISTORY_URL, userId), ImmutableMap.of(SESSION_ID, invalidSessionId))).thenThrow(new HttpResponseException(401, "Unauthorized"));

        api.getWorkoutHistory(ImmutableUserAuth.of(invalidSessionId, userId));
    }

    @Test
    public void testGetWorkoutHistory() throws IOException {
        final String csv = "Workout Timestamp,Live/On-Demand,Instructor Name,Length (minutes),Fitness Discipline,Type,Title,Class Timestamp,Total Output,Avg. Watts,Avg. Resistance,Avg. Cadence (RPM),Avg. Speed (mph),Distance (mi),Calories Burned,Avg. Heartrate,Avg. Incline,Avg. Pace (min/mi)\n" +
                "2020-04-17 12:29 (PDT),On Demand,Cody Rigsby,20,Cycling,Theme,20 min Beginner Ride,2018-08-24 12:11 (PDT),119,99,37%,84,15.81,5.26,164,,,\n" +
                "2020-04-18 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n" +
                "2020-04-19 12:11 (PDT),On Demand,Jess King,30,Cycling,Low Impact,30 min Low Impact Ride,2020-03-30 06:49 (PDT),185,103,39%,80,16.21,8.10,255,,,";

        final String userId = "foo";
        final String sessionId = "bar";
        when(mockHttpClient.get(String.format(PelotonApiAdapter.WORKOUT_HISTORY_URL, userId), ImmutableMap.of(SESSION_ID, sessionId))).thenReturn(csv);

        final List<Workout> workoutHistory = api.getWorkoutHistory(ImmutableUserAuth.of(sessionId, userId));

        final String[] entries = csv.split("\n");
        final Workout workout1 = Workout.parse(entries[1]).get();
        final Workout workout2 = Workout.parse(entries[2]).get();
        final Workout workout3 = Workout.parse(entries[3]).get();

        assertThat(workoutHistory, containsInAnyOrder(workout1, workout2, workout3));
    }

    @Test
    public void getGetWorkoutHistoryWithDateFilter() throws IOException {
        final String csv = "Workout Timestamp,Live/On-Demand,Instructor Name,Length (minutes),Fitness Discipline,Type,Title,Class Timestamp,Total Output,Avg. Watts,Avg. Resistance,Avg. Cadence (RPM),Avg. Speed (mph),Distance (mi),Calories Burned,Avg. Heartrate,Avg. Incline,Avg. Pace (min/mi)\n" +
                "2020-04-15 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n" +
                "2020-04-16 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n" +
                "2020-04-17 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n" +
                "2020-04-18 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n" +
                "2020-04-19 11:01 (PDT),Live,Ben Alldis,30,Cycling,Theme,30 min House Ride,2020-03-07 00:50 (PDT),177,98,40%,75,15.69,7.84,244,,,\n";

        final String userId = "foo";
        final String sessionId = "bar";
        when(mockHttpClient.get(String.format(PelotonApiAdapter.WORKOUT_HISTORY_URL, userId), ImmutableMap.of(SESSION_ID, sessionId))).thenReturn(csv);

        final List<Workout> workoutHistory = api.getWorkoutHistory(ImmutableUserAuth.of(sessionId, userId), LocalDate.of(2020, 4, 16), LocalDate.of(2020, 4, 19));

        final String[] entries = csv.split("\n");
        final Workout workout2 = Workout.parse(entries[2]).get();
        final Workout workout3 = Workout.parse(entries[3]).get();
        final Workout workout4 = Workout.parse(entries[4]).get();

        assertThat(workoutHistory, containsInAnyOrder(workout2, workout3, workout4));
    }
}
