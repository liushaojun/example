package com.brook.example.java8.functions;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 自定义函数 Primes
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/8/1
 */
public class PrimesTest {
    public static Stream<Integer> primes(int n) {
        return Stream.iterate(2, i -> i + 1)
                .filter(PrimesTest::isPrime)
                .limit(n);
    }

    public static boolean isPrime(int candidate) {
        int candidateRoot = (int) Math.sqrt((double) candidate);
        return IntStream.rangeClosed(2, candidateRoot)
                .noneMatch(i -> candidate % i == 0);
    }

}
