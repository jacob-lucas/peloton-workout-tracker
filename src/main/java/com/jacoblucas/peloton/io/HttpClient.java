package com.jacoblucas.peloton.io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClient {
    private final ObjectMapper objectMapper;

    public HttpClient(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T, U> U post(
            final String url,
            final T body,
            final TypeReference<U> responseType
    ) throws IOException {
        final String response = Request.Post(url)
                .bodyString(objectMapper.writeValueAsString(body), ContentType.APPLICATION_JSON)
                .execute()
                .returnContent()
                .asString();
        return objectMapper.readValue(response, responseType);
    }

    public <T> T get(
            final String url,
            final TypeReference<T> responseType
    ) throws IOException {
        return get(url, ImmutableMap.of(), responseType);
    }

    public <T> T get(
            final String url,
            final Map<String, String> cookies,
            final TypeReference<T> responseType
    ) throws IOException {
        final String response = get(url, cookies);
        return objectMapper.readValue(response, responseType);
    }

    public String get(final String url, final Map<String, String> cookies) throws IOException {
        final String cookieStr = cookies.entrySet().stream()
                .map(e -> String.format("%s=%s", e.getKey(), e.getValue()))
                .collect(Collectors.joining(";"));
        return Request.Get(url)
                .addHeader("cookie", cookieStr)
                .execute()
                .returnContent()
                .asString();
    }
}
