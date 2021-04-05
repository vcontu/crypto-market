package com.endava.internship.cryptomarket.confservice.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com/endava/internship/cryptomarket/confservice/api",
        "com/endava/internship/cryptomarket/confservice/service"
})
public class ConfServiceConfig {

}