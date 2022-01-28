package com.endava.upskill.confservice.util;

import java.time.LocalDateTime;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class LocalDateTimeMatcher extends BaseMatcher<String> {

    private final LocalDateTime expected;

    @Override
    public boolean matches(Object actual) {
        if (actual instanceof String s) {
            final LocalDateTime actualLocalDateTime = LocalDateTime.parse(s);
            return actualLocalDateTime.equals(expected);
        }

        return false;
    }

    public LocalDateTimeMatcher(LocalDateTime expected) {
        this.expected = expected;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(expected);
    }

    public static Matcher<String> equalTo(LocalDateTime operand) {
        return new LocalDateTimeMatcher(operand);
    }
}
