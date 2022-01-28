package com.endava.upskill.confservice.util;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import io.restassured.http.Header;
import io.restassured.http.Headers;

public interface Tokens {

     LocalDateTime LDT = LocalDateTime.of(2020, 1,15, 17,35);

     Clock CLOCK_FIXED = Clock.fixed(LDT.toInstant(ZoneOffset.UTC), ZoneId.of("Etc/UTC"));

     Clock CLOCK_CONF_SERVICE = Clock.systemUTC();

     String USERNAME = "username";

     String REQUESTER_USERNAME = "requester";

     String USERNAME_ADMIN = "admin";

     String USERNAME_NOT_EXISTING = "notexisting";

     String EMAIL = "email@gmail.com";

     String REQUESTER_HEADER = "Requester-Username";

     Headers REQUESTER_NULL = new Headers(new Header(REQUESTER_HEADER, null));

     Headers REQUESTER_NOT_EXISTING = new Headers(new Header(REQUESTER_HEADER, USERNAME_NOT_EXISTING));

     Headers REQUESTER_ADMIN = new Headers(new Header(REQUESTER_HEADER, USERNAME_ADMIN));

     Headers REQUESTER_MISSING = new Headers();
}
