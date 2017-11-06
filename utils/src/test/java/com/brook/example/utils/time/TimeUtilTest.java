package com.brook.example.utils.time;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Test;

/**
 * TimeUtil 测试用例
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/8
 */
public class TimeUtilTest {

  @Test
  public void asLocalDate() throws Exception {
    Date now = new Date(117,7,7,20,23);
    LocalDate localDate = TimeUtil.asLocalDate(now);
    String nowStr = TimeUtil.format(localDate);
    assertThat(nowStr).isEqualTo("2017-08-07");
  }

  @Test
  public void asLocalDateTime() throws Exception {
    Date now = new Date(117, 7, 7, 20, 23);
    LocalDateTime localDateTime = TimeUtil.asLocalDateTime(now);
    String nowStr = TimeUtil.format(localDateTime);
    assertThat(nowStr).isEqualTo("2017-08-07 20:23:00");
  }

  @Test
  public void asLocalDateTimeWithZoned() throws Exception {
    Date now = new Date(117, 7, 7, 20, 23);
    LocalDateTime localDateTime = TimeUtil.asLocalDateTime(now, ZoneId.systemDefault());
    String nowStr = TimeUtil.format(localDateTime);
    assertThat(nowStr).isEqualTo("2017-08-07 20:23:00");
  }

  @Test
  public void asLocalDateZoned() throws Exception {
    Date now = new Date(117, 7, 7, 20, 23);
    LocalDate localDate = TimeUtil.asLocalDate(now,ZoneId.systemDefault());
    String nowStr = TimeUtil.format(localDate);
    assertThat(nowStr).isEqualTo("2017-08-07");
  }

}