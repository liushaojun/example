package com.brook.example.java8.functions;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/1
 */
public class CombinatorDemo {
    @Test
    void test() {
        assertThat(repeat(3, (Integer x) -> 2 * x).apply(10)).isEqualTo(80);
    }

    static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
        return x -> g.apply(f.apply(x));
    }

    static <T> Function<T, T> repeat(int n, Function<T, T> f) {
        return n == 0 ? x -> x : compose(f, repeat(n - 1, f));
    }
}
