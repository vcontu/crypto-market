package com.endava.upskill.confservice.api.http;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.endava.upskill.confservice.domain.model.exception.DomainException;

import static com.endava.upskill.confservice.api.http.HttpMethods.DELETE;
import static com.endava.upskill.confservice.api.http.HttpMethods.GET;
import static com.endava.upskill.confservice.api.http.HttpMethods.POST;

import lombok.RequiredArgsConstructor;

class EndpointMatcherTest {
    private final EndpointMatcher endpointMatcher = new EndpointMatcher();

    @ParameterizedTest
    @EnumSource(MatcherTest.class)
    void whenMatchingEndpointThatExists_thenReturnEndpointMatch(MatcherTest test) {
        RequestDetails requestDetails = endpointMatcher.matchEndpoint(test.method, test.requestUrl);
        assertThat(requestDetails.endpoint()).isEqualTo(test.endpoint);
        assertThat(requestDetails.extractedRef()).isEqualTo(test.extract);
    }

    @RequiredArgsConstructor
    private enum MatcherTest {
        T01 (GET, "/conf-service/users/username", Endpoint.GET_USER, "username"),
        T02 (GET, "/conf-service/users/", Endpoint.LIST_USERS, null),
        T03 (POST, "/conf-service/users/", Endpoint.CREATE_USER, null),
        T04 (DELETE, "/conf-service/users/username", Endpoint.DELETE_USER, "username");

        private final String method;
        private final String requestUrl;
        private final Endpoint endpoint;
        private final String extract;
    }

    @ParameterizedTest
    @EnumSource(MatcherTestEx.class)
    void whenMatchingEndpointThatDoesNotExists_thenRaiseException(MatcherTestEx test) {
        assertThatThrownBy(() -> endpointMatcher.matchEndpoint(test.method, test.requestUrl))
                .isEqualTo(DomainException.ofInvalidUrl());
    }

    @RequiredArgsConstructor
    private enum MatcherTestEx {
        T01 (GET, "/conf-service/users/234fs++"),
        T02 (GET, "/conf-service/usersqwfe/"),
        T03 (POST, "/conf-service/users/hello"),
        T04 (DELETE, "/conf-service/users");

        private final String method;
        private final String requestUrl;
    }
}