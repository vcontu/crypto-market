package com.endava.upskill.confservice.application;

import javax.validation.ValidatorFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class ValidationConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean() {

            @Override
            protected void postProcessConfiguration(javax.validation.Configuration<?> configuration) {
                configuration.addProperty( "hibernate.validator.fail_fast", "true" );
            }
        };
    }

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(ValidatorFactory factory) {
        final MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidatorFactory(factory);
        return postProcessor;
    }
}
