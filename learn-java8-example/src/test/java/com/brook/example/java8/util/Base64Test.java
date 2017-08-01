package com.brook.example.java8.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
@Log4j2
class Base64Test {
    @Test
    void base64(TestReporter reporter) {
        final String text = "Base64 for Java 8!";
        final String encoded = Base64
                .getEncoder()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("base64 encoded {}", encoded);
        final String decoded = new String(
                Base64.getDecoder().decode(encoded),
                StandardCharsets.UTF_8);
        assertEquals(text, decoded);
    }
}
