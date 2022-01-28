package com.endava.upskill.confservice.api.http;

public record RequestDetails(Endpoint endpoint, String extractedRef) {

    public RequestDetails(Endpoint endpoint) {
        this(endpoint, null);
    }
}
