package com.brook.example.java8.time;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
public class DateTimeTest {

    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final ThreadLocal<DateFormat> formatters =
            ThreadLocal.withInitial(() -> new SimpleDateFormat(DATETIME_PATTERN));

    @Test
    void useTemporalAdjuster() {
        LocalDate date = LocalDate.of(2014, 3, 18);
        date = date.with(nextOrSame(DayOfWeek.SUNDAY));
        assertThat(date.toString()).isEqualTo("2014-03-23");
        date = date.with(lastDayOfMonth());
        assertThat(date.toString()).isEqualTo("2014-03-31");

        date = date.with(new NextWorkingDay());
        assertThat(date.toString()).isEqualTo("2014-04-01");
        date = date.with(nextOrSame(DayOfWeek.FRIDAY));
        assertThat(date).isEqualTo("2014-04-04");
        date = date.with(new NextWorkingDay());
        assertThat(date).isEqualTo("2014-04-07");

        date = date.with(nextOrSame(DayOfWeek.FRIDAY));
        assertThat(date).isEqualTo("2014-04-11");
        date = date.with(temporal -> {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        });
        assertThat(date).isEqualTo("2014-04-14");
    }

    @Test
    void useDateFormatter() {
        LocalDate date = LocalDate.of(2017, 7, 7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter chinaFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.CHINA);

        assertThat(date.format(DateTimeFormatter.ISO_LOCAL_DATE)).isEqualTo("2017-07-07");
        assertThat(date.format(formatter)).isEqualTo("07/07/2017");
        assertThat(date.format(chinaFormatter)).isEqualTo("七月 2017");

        DateTimeFormatter complexFormatter = new DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH)
                .appendLiteral(" ")
                .appendText(ChronoField.MONTH_OF_YEAR)
                .appendLiteral(" ")
                .appendText(ChronoField.YEAR)
                .parseCaseInsensitive()
                .toFormatter(Locale.CHINESE);

        assertThat(date.format(complexFormatter)).isEqualTo("7 七月 2017");
    }

    @Test
    void useOldDate() {
        Date date = new Date(114, 2, 18);
        System.out.println(formatters.get().format(date));
        Calendar calendar = Calendar.getInstance();
        calendar.set(2014, Calendar.FEBRUARY, 18);
        assertThat(calendar.get(Calendar.MONTH)).isEqualTo(1);
    }

    @Test
    void useLocalDate() {
        LocalDate date = LocalDate.of(2014, 3, 18);
        int year = date.getYear();
        assertThat(year).isEqualTo(2014);
        Month month = date.getMonth(); // MARCH
        assertThat(month).isEqualTo(Month.MARCH);
        int day = date.getDayOfMonth();
        assertThat(day).isEqualTo(18);
        DayOfWeek week = date.getDayOfWeek();
        assertThat(week).isEqualTo(DayOfWeek.TUESDAY);
        int len = date.lengthOfMonth();
        assertThat(len).isEqualTo(31);
        boolean leap = date.isLeapYear();
        assertThat(leap).isFalse();

        int y = date.get(ChronoField.YEAR);
        int m = date.get(ChronoField.MONTH_OF_YEAR);
        int d = date.get(ChronoField.DAY_OF_MONTH);
        Assertions.assertAll("LocalDate ",()->{
            Assertions.assertEquals(2014, y);
            Assertions.assertEquals(3, m);
            Assertions.assertEquals(18, d);
        });
        LocalTime time = LocalTime.of(13, 45, 20);
       assertThat(time.toString()).isEqualTo("13:45:20");

        LocalDateTime dt1 = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45, 20);
        LocalDateTime dt2 = LocalDateTime.of(date, time);
        LocalDateTime dt3 = date.atTime(13, 45, 20);
        LocalDateTime dt4 = date.atTime(time);
        LocalDateTime dt5 = time.atDate(date);
        assertThat(dt1).isEqualTo( "2014-03-18T13:45:20");
        assertThat(dt1).isEqualTo(dt2)
                       .isEqualTo(dt3)
                       .isEqualTo(dt4)
                       .isEqualTo(dt5);
        Instant instant = Instant.ofEpochSecond(44 * 365 * 86400);
        Instant now = Instant.now();

        Duration d1 = Duration.between(LocalTime.of(13, 45, 10), time);
        Duration d2 = Duration.between(instant, now);
        // Duration 和 java.time.Period 区别
        // Duration 表示 LocalDateTime  而 Period  只是LocalDate
        assertThat(d1.getSeconds()).isEqualTo(10);

        Duration threeMinutes = Duration.of(3, ChronoUnit.MINUTES);
        assertThat(threeMinutes.getSeconds()).isEqualTo(180);
        JapaneseDate japaneseDate = JapaneseDate.from(date);
        System.out.println(japaneseDate);
    }
    @Test
    void convert(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = DateConvertUtils.asUtilDate(localDateTime);
        Date date1 = new Date();
        assertThat(date).isEqualToIgnoringSeconds(date1);
        String localDateTimeStr = DateConvertUtils.asLocalDateTime(date1)
                .format(DateTimeFormatter.ofPattern(DATETIME_PATTERN));
        assertThat(localDateTimeStr).isEqualTo(localDateTime
                .format(DateTimeFormatter.ofPattern(DATETIME_PATTERN)));
    }

    private static class NextWorkingDay implements TemporalAdjuster {
        @Override
        public Temporal adjustInto(Temporal temporal) {
            DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
            int dayToAdd = 1;
            if (dow == DayOfWeek.FRIDAY) dayToAdd = 3;
            if (dow == DayOfWeek.SATURDAY) dayToAdd = 2;
            return temporal.plus(dayToAdd, ChronoUnit.DAYS);
        }
    }
}
