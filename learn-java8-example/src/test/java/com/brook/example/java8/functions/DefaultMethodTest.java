package com.brook.example.java8.functions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 默认方法
 * <p>
 * 1. 默认方法
 * 2. 多继承
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
public class DefaultMethodTest {

    @Test
    void getName(){
        TomPlayer player = new TomPlayer();
        assertEquals("tom",player.getName());
        assertTrue(player.isMale());
        Player.foo();
    }

    /**
     * 多重继承
     */
    @Test
    void foobar(){
        Foobar foobar = new Foobar();
        assertEquals("bar",foobar.name());
    }
}
interface Player {
    String getName();
    default boolean isMale(){
        return true;
    }

    static void foo(){
        System.out.println("我是静态方法");
    }
}
class TomPlayer implements Player{
    @Override
    public String getName() {
        return "tom";
    }
}
class LucyPlayer implements Player{
    @Override
    public boolean isMale() {
        return false;
    }

    @Override
    public String getName() {
        return "lucy";
    }
}

interface Foo{
    default String name() {
       return "foo";
    }
}

interface Bar{
    default String name() {
        return "bar";
    }
}
class Foobar implements Foo, Bar {
    // 菱形问题
    // 如果覆盖 name() 编译器就会报错，
    @Override
    public String name() {
        return Bar.super.name();
    }
}