package com.brook.example.jmh;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * 采用main方法进行测试或打包进行执行jar文件测试
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/1/29
 */
public class App {

  public static void main(String[] args) throws RunnerException {
    Options opt = new OptionsBuilder()
        .include(EnumMapTest.class.getSimpleName())
        .build();

    new Runner(opt).run();
  }
}
