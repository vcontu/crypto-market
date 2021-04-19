package com.endava.upskill.confservice.service;

import org.springframework.stereotype.Component;

@Component
public class HelloServiceImpl implements HelloService {

    @Override
    public String getResponse() {
        return "Hello World!";
    }
}
