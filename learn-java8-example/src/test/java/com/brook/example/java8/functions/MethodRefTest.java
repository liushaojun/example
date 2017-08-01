package com.brook.example.java8.functions;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
@Log4j2
public class MethodRefTest {
    @Test
    void test(){
        new ConcurrentGreeter().greet();
    }
}
@Log4j2
class Greeter {
    public void greet(){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("Greet !");
    }
}
@Log4j2
class ConcurrentGreeter extends Greeter{
    @Override
    public void greet() {

        Thread t = new Thread(super::greet); // 原型就是 ()->super.greet();
        t.start();
        log.info("------ Output Greet !-------");
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("-------- End !--------");
    }
}