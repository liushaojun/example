package com.brook.example.jooq;

import com.brook.example.jooq.tables.MonitorInfo;
import com.brook.example.jooq.tables.records.MonitorInfoRecord;
import java.sql.Connection;
import java.sql.DriverManager;
import lombok.Data;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/11/23
 */
public class JOOQTest {
  public static final String DB_URL =
      "jdbc:mysql://192.168.1.90:3308/caishen_backstage";
  public static final String DB_USERNAME="yanfa";
  public static final String DB_PASSWORD="yanfa#123";

  private DSLContext dslContext;

  @Before
  public void before() {
    this.dslContext = getDSLContext();
  }

  @Test
  public void insert() {
    MyMonitorInfo info = new MyMonitorInfo();

    info.setName("foo");
    info.setErrorResult("error");
    info.setId(100L);
    MonitorInfoRecord record = dslContext.newRecord(Tables.MONITOR_INFO, info);
    record.insert();

    dslContext.insertInto(Tables.MONITOR_INFO)
        .set(MonitorInfo.MONITOR_INFO.NAME, "bar")
        .set(MonitorInfo.MONITOR_INFO.URL, "https://baidu.com")
        .execute();
  }

  @Test
  public void find() {
    dslContext.selectFrom(Tables.MONITOR_INFO)
        .where(MonitorInfo.MONITOR_INFO.NAME.like("csa%"))
        .fetchInto(MyMonitorInfo.class)
        .stream()
        .forEach(e -> System.out.println(e.getName()));
  }

  @Test
  public void update() {

    dslContext.update(Tables.MONITOR_INFO)
        .set(MonitorInfo.MONITOR_INFO.DESC, "csapp desc")
        .set(MonitorInfo.MONITOR_INFO.NAME, "csapp 元宝")
        .where(MonitorInfo.MONITOR_INFO.ID.eq(3L))
        .execute();
  }

  @After
  public void after() {
//    dslContext.delete(Tables.MONITOR_INFO);
  }

  private DSLContext getDSLContext() {
    try {
      Connection connection =
          DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
      return DSL.using(connection, SQLDialect.MYSQL);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  @Data
  public static class MyMonitorInfo {
    private Long id;

    private String name;
    private String errorResult;
  }

}
