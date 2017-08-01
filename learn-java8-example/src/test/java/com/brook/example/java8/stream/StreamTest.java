package com.brook.example.java8.stream;

import com.brook.example.java8.domain.Person;
import com.brook.example.java8.stream.forker.Dish;
import com.google.common.collect.Lists;
import com.google.common.math.IntMath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.TestReporter;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.stream;

/**
 * java8 stream example
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/28
 */
@Log4j2
public class StreamTest {

    @TestFactory
    Stream<DynamicTest> range(){
        // numeric ranges
        long evenCount = IntStream.rangeClosed(1, 100)
                .filter(n -> n % 2 == 0)
                .count();

        assertEquals(50,evenCount);
        //毕氏三元数;

        Stream<int[]> pythagoreanTriples =
                IntStream.rangeClosed(1, 100)
                        .boxed()
                        .flatMap(a -> IntStream.rangeClosed(a, 100)
                                .filter(b -> Math.sqrt(a * a + b * b) % 1 == 0)
                                .boxed()
                                .map(b -> new int[]{a, b, (int) Math.sqrt(a * a + b * b)}));
            return stream(
                    pythagoreanTriples.iterator()
                    ,ints -> ints[0]+"² +"+ints[1]+"²  = "+ints[2]+"²"
                    ,(ints)-> assertEquals(IntMath.pow(ints[2],2), IntMath.pow(ints[0],2)+ IntMath.pow(ints[1],2))
            );
    }
    @Test
    void min() {
        int min = IntStream
                .of(5, 3, 1, 6)
                .min()
                .orElse(-1);
        assertEquals(1, min, "This' min element is 1");
    }

    /**
     * 自然排序
     * <p>
     * {@link Stream#sorted()}
     *
     * @param reporter
     */
    @Test
    void sort(TestReporter reporter) {
        List<Integer> sorts = IntStream.of(5, 3, 1, 6)
                .boxed()
                .sorted()
                .collect(Collectors.toList());
        assertIterableEquals(sorts, Lists.newArrayList(1, 3, 5, 6));
        reporter.publishEntry("排序(asc)后结果:", sorts.toString());
    }

    @Test
    void sortWithComparator(TestReporter reporter) {
        List<Integer> sorts = IntStream.of(5, 3, 1, 6)
                // .map(Integer::valueOf)
                .boxed() // 源码就是 .map(Integer::valueOf)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        assertIterableEquals(sorts, Lists.newArrayList(6, 5, 3, 1));
        reporter.publishEntry("排序(desc)后结果:", sorts.toString());
    }

    @Test
    void sortWithComparing(TestReporter reporter) {
        Person tom = Person
                .builder()
                .id(1L)
                .age(20)
                .name("tom")
                .build();
        Person jack = Person.builder()
                .id(2L)
                .age(20)
                .name("jack")
                .build();
        List<Person> persons = Lists.newArrayList(tom, jack);
        persons.stream()
                .sorted(Comparator.comparing(Person::getName)
                        .thenComparing(Person::getAge))
                .forEach((p) -> reporter.publishEntry(p.getName(), p.getAge() + ""));
    }

    @Test
    void listToMap(TestReporter reporter) {
        List<String> languages = getLanguages();
        Map result = languages.parallelStream()
                .collect(Collectors.toMap(Object::toString, String::toUpperCase));
        reporter.publishEntry(result);
    }

    private List<String> getLanguages() {
        return Lists.newArrayList("java", "php", "python");
    }

    @Test
    void flatMap(TestReporter reporter) {
        List<List<String>> lists = Lists.newArrayList();
        lists.add(Arrays.asList("apple", "banana", "pear"));
        lists.add(Arrays.asList("email", "weibo", "qq", "wechat"));
        lists.add(Arrays.asList("c#", "java", "php", "python"));
        long total = lists.stream()
                // flatMap 可以减少一次循环
                .flatMap(Collection::stream)
                .filter(str -> str.length() > 3)
                .count();
        assertEquals(8, total);
    }

    @Test
    void match() {
        List<String> words = Lists.newArrayList("abc", "acb", "ace");
        boolean allMathch = words.stream()
                .allMatch(w -> w.startsWith("a"));
        assertTrue(allMathch);
        boolean noneMatch = words.stream()
                .noneMatch(w -> w.contains("z"));
        assertTrue(noneMatch, "words does'nt contains z!");
    }

    @Test
    void counter() {
        long count = getLanguages()
                .stream()
                .filter((s) -> s.contains("p"))
                .count();
        assertEquals(2, count, "The number of languages containing p is 2");
    }

    @Test
    void filter() {
        // 过滤出素食的菜单
        List<Dish> vegetarianMenu =
                Dish.MENU.stream()
                        .filter(Dish::isVegetarian)
                        .collect(toList());
        assertEquals("french fries", vegetarianMenu.get(0).getName());
        // 过滤 能量大于300的3个菜单
        List<Dish> dishesLimit3 =
                Dish.MENU.stream()
                        .filter(d -> d.getCalories() > 300)
                        .limit(3)
                        .collect(toList());
        assertEquals(3, dishesLimit3.size());
    }

    /**
     * 分组分片
     */
    @Test
    void groupBy() {
        Map<String, List<Locale>> localsOfCountry = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.groupingBy(Locale::getCountry));
        localsOfCountry.get("CH")
                .stream()
                .map(Locale::getLanguage)
                .forEach(System.out::println);
        // 按国家代号统计语言总数
        Map<String, Long> countLocalsOfCountry = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.groupingBy(Locale::getCountry, Collectors.counting()));
        long count = countLocalsOfCountry.get("CH");
        assertEquals(3, count);
    }

    @Test
    void distinct(TestReporter reporter) {
        String result = getLanguages()
                .stream()
                .map(s -> s.split(""))
                .flatMap(Stream::of)
                .distinct()
                .map(String::valueOf) // 注意： 这里尽可能较少的处理
                .reduce(String::concat)
                .orElse("");
        assertEquals("javphyton", result);
        reporter.publishEntry("result >>>", result);

    }

    @Test
    void reduce(TestReporter reporter) {
        String result = getLanguages().stream()
                .reduce((a, b) -> a.concat(";").concat(b))
                .orElse("");
        assertEquals(result, String.join(";", getLanguages()));
        reporter.publishEntry("result is >>>", result);
        int totalWords = getLanguages().stream()
                .map(String::length)
                .reduce(Integer::sum)
                .orElse(0);
        assertEquals(13, totalWords);
    }

    @Test
    void generate() {
        List<Long> result = Stream
                .generate(new FibonacciSupplier())
                .peek(log::info)
                .skip(5)
                .limit(5)
                .collect(Collectors.toList());
        log.info("result is >>> {}", result);
    }

    @Test
    void iterate() {
        Stream.iterate(0, n -> n + 2)
                .limit(10)
                .map(n -> n + " ")
                .forEach(System.out::print);
    }


    class FibonacciSupplier implements Supplier<Long> {

        long a = 0;
        long b = 1;

        @Override
        public Long get() {
            long x = a + b;
            a = b;
            b = x;
            return a;
        }
    }
}