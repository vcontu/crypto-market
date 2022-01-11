package com.endava.upskill.confservice;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelloWorldTest {
    @Test
    void exceptionsNotThrown() {
        assertDoesNotThrow(() -> HelloWorld.main(null));
    }
}
