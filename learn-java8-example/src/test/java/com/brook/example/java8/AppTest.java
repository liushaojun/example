package com.brook.example.java8;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.stream;

/**
 * Unit test for simple App.
 */
@DisplayName("junit5 testcase.")
class AppTest {

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }

    @Test
    @DisplayName("junit5 helloJUnit5.")
    void helloJUnit5() {
        assertEquals("hello junit5", "hello junit5");
    }

    /**
     * use {@code {@link ParameterizedTest}} annotation ，
     * required dependency on the <code>junit-jupiter-params</code> artifact.
     * @param argument
     */
    @ParameterizedTest
    @ValueSource(strings = { "Hello", "World" })
    void testWithStringParameter(String argument) {
        assertNotNull(argument);
    }

    @TestFactory
    Stream<DynamicTest> streamDynamicTest() {
        return stream(
                Stream.of("Hello", "World").iterator(),
                (word) -> String.format("Test - %s", word),
                (word) -> {
                    assertTrue(word.length() > 4);
                    assertNotNull(word);

                });
    }

    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument) {
        assertNotEquals(9, argument);
    }


}
