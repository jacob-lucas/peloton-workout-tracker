package com.jacoblucas.peloton.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jacoblucas.peloton.io.HttpClient;

public final class PelotonApiAdapterFactory {
    private PelotonApiAdapterFactory() {}

    public static PelotonApiAdapter instance() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new GuavaModule());

        return new PelotonApiAdapter(new HttpClient(objectMapper));
    }
}
