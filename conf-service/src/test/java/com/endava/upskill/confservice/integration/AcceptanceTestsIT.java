package com.endava.upskill.confservice.integration;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * All unit tests should be isolated and independent of each other. Is this possible with AcceptanceTests Tests as well? Acceptance Tests do not access the
 * internals of the application (in our case it is not even possible). The test engine and system under test are two separate processes. We do not have access
 * internal state of the application (no DB to call). Neither can we populate nor clean up the internal state through
 * other means than using the API.<br/> For
 * this reason, our tests in this class are ordered and depend on each other. You need to run them all to actually identify where potentially the problem is.
 */
@Suite
@SelectClasses({
        UsersGeneralFlowAT.class,
        CommonExceptionsAT.class,
        UsersCreateAT.class,
        UsersGetAT.class,
        UsersListAT.class,
        UsersDeleteAT.class
})
@SuiteDisplayName("/users Acceptance Tests")
public class AcceptanceTestsIT {

}
