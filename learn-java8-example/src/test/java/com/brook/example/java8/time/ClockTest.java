package com.brook.example.java8.time;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/29
 */
@Log4j2
public class ClockTest {
    @Test
    void clock() {
        Clock clock = Clock.systemDefaultZone();
        log.info("current millis is {}",clock.millis());
        Instant instant = clock.instant();
        Date now = Date.from(instant);
        log.info("current times is {}",now.getTime());

    }
}
