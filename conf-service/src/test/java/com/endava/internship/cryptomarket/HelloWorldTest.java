package com.endava.internship.cryptomarket;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HelloWorldTest {
    @Test
    void exceptionsNotThrown() {
        assertDoesNotThrow(() -> HelloWorld.main(null));
    }
}
