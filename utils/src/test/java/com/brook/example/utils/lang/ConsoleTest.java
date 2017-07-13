package com.brook.example.utils.lang;

import org.junit.Test;

/**
 * ${DESCRIPTION}
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
public class ConsoleTest {
    @Test
    public void log() throws Exception {
        Console.log();
        Console.log("Hello World for Console.");
        Console.log("Hello World for {}.","Console");
        Console.log(()-> "Console for lambda.");
    }

}