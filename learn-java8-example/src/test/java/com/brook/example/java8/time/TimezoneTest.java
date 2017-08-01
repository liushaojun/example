package com.brook.example.java8.time;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/29
 */
@Log4j2
class TimezoneTest {
    @Test
    void test() {
        log.info("zoneIds is {}", ZoneId.getAvailableZoneIds());
        ZoneId zone1 = ZoneId.of("Europe/Berlin");
        ZoneId zone2 = ZoneId.of("Brazil/East");
        log.info("zone1 rules is {} ", zone1.getRules());
        log.info("zone1 rules is {} ", zone2.getRules());
    }
}
