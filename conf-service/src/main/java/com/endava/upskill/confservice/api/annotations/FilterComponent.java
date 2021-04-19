package com.endava.upskill.confservice.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import org.springframework.stereotype.Component;

@Target(TYPE)
@Retention(RUNTIME)
@Component
public @interface FilterComponent {

    String path();
}
