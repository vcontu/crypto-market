package com.endava.upskill.confservice.api.http;

import java.util.regex.Matcher;

import org.springframework.stereotype.Component;

import com.endava.upskill.confservice.domain.model.exception.DomainException;

@Component
public class EndpointMatcher {

    private static final int FIRST_GROUP_REGEX_EXTRACT = 1;

    public RequestDetails matchEndpoint(String httpMethod, String requestPath) throws DomainException {
        for (Endpoint endpoint : Endpoint.values()) {
            final Matcher regexMatcher = endpoint.getMatcher(requestPath);
            if (endpoint.getMethod().equals(httpMethod) && regexMatcher.matches()) {
                if (endpoint.isExtractable()) {
                    String extractedRef = regexMatcher.group(FIRST_GROUP_REGEX_EXTRACT);
                    return new RequestDetails(endpoint, extractedRef);
                } else {
                    return new RequestDetails(endpoint);
                }

            }
        }
        throw DomainException.ofInvalidUrl();
    }
}
