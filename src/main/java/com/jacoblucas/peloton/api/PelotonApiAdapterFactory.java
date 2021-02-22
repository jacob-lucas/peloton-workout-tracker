package com.jacoblucas.peloton.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jacoblucas.peloton.io.HttpClient;
import com.jacoblucas.peloton.utils.ObjectMapperFactory;

public final class PelotonApiAdapterFactory {
    private PelotonApiAdapterFactory() {}

    public static PelotonApiAdapter instance() {
        final ObjectMapper objectMapper = ObjectMapperFactory.instance();
        return new PelotonApiAdapter(new HttpClient(objectMapper));
    }
}
