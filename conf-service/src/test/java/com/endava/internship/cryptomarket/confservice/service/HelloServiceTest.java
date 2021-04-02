package com.endava.internship.cryptomarket.confservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloServiceTest {

    private HelloService testService;

    @BeforeEach
    void setUp() {
        testService = new HelloServiceImpl();
    }

    @Test
    void whenGetResponse_thenReturnHelloWorld() {
        assertThat(testService.getResponse()).isEqualTo("Hello World!");
    }

}
