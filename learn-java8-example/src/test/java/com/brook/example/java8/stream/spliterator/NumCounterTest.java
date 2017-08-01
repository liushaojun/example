package com.brook.example.java8.stream.spliterator;

import org.junit.jupiter.api.Test;

import java.util.Spliterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
public class NumCounterTest {
    final static String arr = "12%3 21sdas s34d dfsdz45   R3 jo34 sjkf8 3$1P 213ikflsd fdg55 kfd";

    private static int countNum(Stream<Character> stream) {
        NumCounter numCounter = stream
                .reduce(new NumCounter(0, 0, false)
                , NumCounter::accumulate
                ,NumCounter::combine);
        return numCounter.getSum();
    }

    @Test
    void counter() {
        Stream<Character> stream = IntStream.range(0, arr.length()).mapToObj(arr::charAt);
        System.out.println("ordered total: " + countNum(stream));

        Spliterator<Character> spliterator = new NumCounterSpliterator(arr);
        // 传入true表示是并行流
        Stream<Character> parallelStream = StreamSupport.stream(spliterator, true);
        System.out.println("parallel total: " + countNum(parallelStream));
    }

}
