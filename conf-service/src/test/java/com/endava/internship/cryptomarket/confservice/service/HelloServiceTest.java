package com.endava.internship.cryptomarket.confservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloServiceTest {

    private HelloService testService;

    @BeforeEach
    void setUp() {
        testService = new HelloServiceImpl();
    }

    @Test
    void whenGetResponse_thenReturnHelloWorld() {
        assertEquals("Hello World!", testService.getResponse());
    }

}
