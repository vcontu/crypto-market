package com.endava.internship.cryptomarket.confservice.integration;

import com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses;
import com.endava.internship.cryptomarket.confservice.business.model.UserDTO;
import com.endava.internship.cryptomarket.confservice.data.model.Roles;
import com.endava.internship.cryptomarket.confservice.data.model.Status;
import com.endava.internship.cryptomarket.confservice.data.model.User;
import io.restassured.http.Method;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.endava.internship.cryptomarket.confservice.business.exceptions.ExceptionResponses.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Roles.*;
import static com.endava.internship.cryptomarket.confservice.data.model.Status.*;
import static io.restassured.http.Method.*;

@RequiredArgsConstructor
@Getter
public enum ApiErrorTest {
    TEST1(POST, "http://localhost:8080/conf-service/users", "admin",
            "text/plain", NOT_ACCEPTABLE_CONTENT, "Should be a request object", ""),
    TEST2(PUT, "http://localhost:8080/conf-service/users/operat1", "admin",
            "text/plain", NOT_ACCEPTABLE_CONTENT, "Should be a request object", ""),
    TEST3(GET, "http://localhost:8080/conf-service/users", null, "application/json; charset: UTF-8", AUTHENTICATION_FAILURE,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST4(GET, "http://localhost:8080/conf-service/users/operat1", null, "application/json; charset: UTF-8", AUTHENTICATION_FAILURE,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST5(POST, "http://localhost:8080/conf-service/users", null, "application/json; charset: UTF-8", AUTHENTICATION_FAILURE,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST6(PUT, "http://localhost:8080/conf-service/users/operat1", null, "application/json; charset: UTF-8", AUTHENTICATION_FAILURE,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST7(DELETE, "http://localhost:8080/conf-service/users/operat1", null, "application/json; charset: UTF-8", AUTHENTICATION_FAILURE,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST8(GET, "http://localhost:8080/conf-service/users", "operat", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat"),
    TEST9(GET, "http://localhost:8080/conf-service/users/operat1", "operat", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat"),
    TEST10(POST, "http://localhost:8080/conf-service/users", "operat", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat"),
    TEST11(PUT, "http://localhost:8080/conf-service/users/operat1", "operat", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat"),
    TEST12(DELETE, "http://localhost:8080/conf-service/users/operat1", "operat", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat"),
    TEST13(GET, "http://localhost:8080/conf-service/users", "client1", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "client1"),
    TEST14(GET, "http://localhost:8080/conf-service/users/operat1", "client1", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "client1"),
    TEST15(POST, "http://localhost:8080/conf-service/users", "client1", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "client1"),
    TEST16(PUT, "http://localhost:8080/conf-service/users/operat1", "client1", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "client1"),
    TEST17(DELETE, "http://localhost:8080/conf-service/users/operat1", "client1", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "client1"),
    TEST18(GET, "http://localhost:8080/conf-service/users", "operat4", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST19(GET, "http://localhost:8080/conf-service/users/operat1", "operat4", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST20(POST, "http://localhost:8080/conf-service/users", "operat4", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST21(PUT, "http://localhost:8080/conf-service/users/operat1", "operat4", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST22(DELETE, "http://localhost:8080/conf-service/users/operat1", "operat4", "application/json; charset: UTF-8", NONEXISTENT_USER_NOT_AUTHORIZED,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST23(GET, "http://localhost:8080/conf-service/users", "operat3", "application/json; charset: UTF-8", USER_SUSPND_ACCESS_FORBIDDEN,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat3"),
    TEST24(GET, "http://localhost:8080/conf-service/users/operat1", "operat3", "application/json; charset: UTF-8", USER_SUSPND_ACCESS_FORBIDDEN,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat3"),
    TEST25(POST, "http://localhost:8080/conf-service/users", "operat3", "application/json; charset: UTF-8", USER_SUSPND_ACCESS_FORBIDDEN,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat3"),
    TEST26(PUT, "http://localhost:8080/conf-service/users/operat1", "operat3", "application/json; charset: UTF-8", USER_SUSPND_ACCESS_FORBIDDEN,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat3"),
    TEST27(DELETE, "http://localhost:8080/conf-service/users/operat1", "operat3", "application/json; charset: UTF-8", USER_SUSPND_ACCESS_FORBIDDEN,
            new UserDTO("operat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat3"),
    TEST28(DELETE, "http://localhost:8080/conf-service/users/operat2", "operat1", "application/json; charset: UTF-8", USER_NOT_ALLOWED_REMOVE,
            new UserDTO("operat2", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST29(GET, "http://localhost:8080/conf-service/users/operator", "admin", "application/json; charset: UTF-8", USER_NOT_FOUND,
            new UserDTO("operator", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operator"),
    TEST30(PUT, "http://localhost:8080/conf-service/users/operator", "admin", "application/json; charset: UTF-8", USER_NOT_FOUND,
            new UserDTO("operator", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operator"),
    TEST31(DELETE, "http://localhost:8080/conf-service/users/operator", "admin", "application/json; charset: UTF-8", USER_NOT_FOUND,
            new UserDTO("operator", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operator"),
    TEST32(PUT, "http://localhost:8080/conf-service/users/operat1", "operat1", "application/json; charset: UTF-8", SELF_AMEND,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST33(POST, "http://localhost:8080/conf-service/users", "operat1", "application/json; charset: UTF-8",
            JSON_MALFORMED, "Corrupted JSON", ""),
    TEST34(PUT, "http://localhost:8080/conf-service/users/operat2", "operat1", "application/json; charset: UTF-8",
            JSON_MALFORMED, "Corrupted JSON", ""),
    TEST35(POST, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", ADMIN_NOT_ALLOWED_CHANGE_ADMIN,
            new UserDTO("newadmin", "admin@gmail.com", ADMIN, ACTIVE, null, null, null), "admin"),
    TEST36(POST, "http://localhost:8080/conf-service/users", "operat1", "application/json; charset: UTF-8", OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT,
            new UserDTO("newadmin", "admin@gmail.com", ADMIN, ACTIVE, null, null, null), "operat1"),
    TEST37(POST, "http://localhost:8080/conf-service/users", "operat1", "application/json; charset: UTF-8", OPERAT_NOT_ALLOWED_CHANGE_ADMIN_OPERAT,
            new UserDTO("newoperat", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST38(PUT, "http://localhost:8080/conf-service/users/theOperat", "admin", "application/json; charset: UTF-8", DIFFERENT_USERNAME,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), ""),
    TEST39(PUT, "http://localhost:8080/conf-service/users/operat1", "admin", "application/json; charset: UTF-8", USER_NOT_CHANGED,
            new UserDTO("operat1", null, null, null, null, null, null), ""),
    TEST40(POST, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", USER_MISSING_PROPERTY,
            new UserDTO("operat1", null, null, null, null, null, null), ""),
    TEST41(POST, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", USER_ILLEGAL_STATE,
            new UserDTO("operat8", "invalid email", OPERAT, ACTIVE, null, null, null), ""),
    TEST42(PUT, "http://localhost:8080/conf-service/users/operat1", "admin", "application/json; charset: UTF-8", USER_ILLEGAL_STATE,
            new UserDTO("operat1", "invalid email", OPERAT, ACTIVE, null, null, null), ""),
    TEST43(PUT, "http://localhost:8080/conf-service/users/operat4", "admin", "application/json; charset: UTF-8", INACTIV_USER_AMEND,
            new UserDTO("operat4", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat4"),
    TEST44(POST, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", INACTIV_USER_CREATE,
            new UserDTO("operat5", "operat@gmail.com", OPERAT, INACTV, null, null, null), "operat5"),
    TEST45(POST, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", USER_ALREADY_EXISTS,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST46(GET, "http://localhost:8080/conf-service/users/operat/count", "admin", "application/json; charset: UTF-8", NONEXISTENT_ENDPOINT,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST47(PUT, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", NONEXISTENT_ENDPOINT,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST48(POST, "http://localhost:8080/conf-service/users/operat", "admin", "application/json; charset: UTF-8", NONEXISTENT_ENDPOINT,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1"),
    TEST49(DELETE, "http://localhost:8080/conf-service/users", "admin", "application/json; charset: UTF-8", NONEXISTENT_ENDPOINT,
            new UserDTO("operat1", "operat@gmail.com", OPERAT, ACTIVE, null, null, null), "operat1");

    private final Method method;
    private final String urlPath;
    private final String RequesterUsername;
    private final String contentType;
    private final ExceptionResponses exceptionResponses;
    private final Object messageParam;
    private final String exceptionParam;
}
