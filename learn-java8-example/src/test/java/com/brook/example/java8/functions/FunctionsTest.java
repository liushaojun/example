package com.brook.example.java8.functions;

import com.brook.example.java8.domain.Person;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/30
 */
@Log4j2
public class FunctionsTest {
    /**
     * @see com.brook.example.java8.pattern.StrategyPattern
     */
    @Test
    void predicate(){
        Predicate<String> predicate = (s) -> s.length() > 0;
        assertTrue(predicate.test("foo"));
        assertFalse(predicate.negate().test("foo"));
        Predicate<Boolean> nonNull = Objects::nonNull;
        assertTrue(nonNull.test(Boolean.FALSE));
        Predicate<Boolean> isNull = Objects::isNull;
        assertTrue(isNull.test(null));

        Predicate<String> isEmpty = String::isEmpty;
        assertTrue(isEmpty.test(""));
        Predicate<String> isNotEmpty = isEmpty.negate();
        assertTrue(isNotEmpty.test("Not Empty."));
    }

    /**
     * 案例请看
     * @see com.brook.example.java8.pattern.ChainPattern
     */
    @Test
    void function() {
        Function<String, Integer> toInteger = Integer::valueOf;
        Function<String, String> backToString = toInteger.andThen(String::valueOf);
        assertEquals("123", backToString.apply("123"));
        // andThen 和 compose 区别
        Function<Integer, Integer> times2 = e -> e * 2;
        Function<Integer, Integer> sqr = e -> e * e;
        int r1 = times2.compose(sqr).apply(3); // 18
        int r2 = times2.andThen(sqr).apply(3); // 36
        // 结果说明 compose 先执行参数,而andThen 先执行函数调用者
        assertEquals(r1,18);
        assertEquals(r2,36);
    }

    /**
     * @see com.brook.example.java8.pattern.ObserverPattern
     */
    @Test
    void consumer() {
        Consumer<Person> greeter = (p) -> log.info("Hello, " + p.getName());
        greeter.accept(new Person(1L, "tom"));
    }

    /**
     * @see com.brook.example.java8.pattern.FactoryPattern
     */
    @Test
    void supplier(){
        Supplier<Person> create = Person::new;
        Person p = create.get();
        assertNotNull(p);
    }
}
