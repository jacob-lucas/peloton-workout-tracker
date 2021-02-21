package com.jacoblucas.peloton.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

// See https://docs.postman-echo.com/
public class HttpClientTest {
    private static ObjectMapper OBJECT_MAPPER;

    private HttpClient httpClient;

    @BeforeClass
    public static void setUpSuite() {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new Jdk8Module());
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.registerModule(new GuavaModule());
    }

    @Before
    public void setUp() {
        httpClient = new HttpClient(OBJECT_MAPPER);
    }

    @Test
    public void testPost() throws IOException {
        final Map<String, Object> request = ImmutableMap.of("A", 1);
        final Map<String, Object> response = httpClient.post("https://postman-echo.com/post", request, new TypeReference<Map<String, Object>>() {});
        assertThat(response, is(notNullValue()));
    }

    @Test
    public void testGet() throws IOException {
        final Map<String, Object> response = httpClient.get("https://postman-echo.com/get", new TypeReference<Map<String, Object>>() {});
        assertThat(response, is(notNullValue()));
    }
}
