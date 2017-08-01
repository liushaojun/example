package com.brook.example.java8.collections;

import com.brook.example.java8.domain.Person;
import com.google.common.collect.Maps;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * java8 Map test
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/29
 */
@Log4j2
public class MapTest {
    @Test
    void test(TestReporter reporter){
        Map<String, Integer> map = Maps.newHashMapWithExpectedSize(16);
        map.putIfAbsent("a",1);
        int val = map.putIfAbsent("a",2);
        assertEquals(1,val);
        Integer val3 = map.putIfAbsent("b",3);
        assertNull(val3);
        int val4 = map.put("b",4);
        assertEquals(3,val4);
        Integer val5 = map.put("c",5);
        assertNull(val5);
        map.computeIfAbsent("c", key -> 6 );
        assertNotNull(map.get("c"));
        map.computeIfPresent("b",(k,v)-> v * 2);
        assertEquals(8, map.get("b").intValue());
        int val6 = map.getOrDefault("d",-1);
        assertEquals(-1,val6);
        map.put("d",null);
        int val7 = map.merge("d",6,(v,newVal)->newVal);
        assertEquals(6,val7);
        int val8 = map.merge("d", 8,(v,newVal)->newVal+v);
        assertEquals(14,val8);
        log.info("map is >>>{}",map);
    }
    @Test
    void testCurrentHashMap(){
        ConcurrentHashMap<String,Long> map = new ConcurrentHashMap<>();
        String word = "test";
        // 以下这段代码不是原子性，因为另一个线程可能同时在更新相同的计数
        Long oldVal = map.get(word);
        Long newVal = oldVal == null ?1 : oldVal +1;
        map.put(word,newVal);
        // 1. 可以使用replace
        do {
            oldVal = map.get(word);
            newVal = oldVal == null ?1 : oldVal + 1;
        }while (!map.replace(word,oldVal,newVal));
        // 2. 可以使用 java8 LongAdder
        ConcurrentHashMap<String ,LongAdder> laMap = new ConcurrentHashMap<>();
        laMap.putIfAbsent(word,new LongAdder());
        laMap.get(word).increment();
        // 如果是复杂计算可以用, val 不能为null
        map.compute(word, (k, v) -> v == null ? 1 : v + 1);
        // putIfAbsent 区别，只有在需要计算时才会被调用
        laMap.computeIfAbsent(word, key -> new LongAdder());
        laMap.get(word).increment();
        // 或者
        map.merge(word,1L,Long::sum);
        //注意 传递给 compute 和 merge 方法的函数不能为null，否则已有的数据项会从映射中删除
        // 小心使用compute 和 merge 方法，牢记不要进行大量操作
    }

    /**
     *  U searchKeys(long threshold, BiFunction<? super K, ? extends U> f)
        U searchValues(long threshold, BiFunction<? super V, ? extends U> f)
        U search(long threshold, BiFunction<? super K, ? super V,? extends U> f)
        U searchEntries(long threshold, BiFunction<Map.Entry<K, V>, ? extends U> f）
     */
    @Test
    void expand(){
        ConcurrentHashMap<String,Integer> map = (ConcurrentHashMap<String, Integer>) Stream.generate(new
                RandomPerson())
                .limit(100)
                .collect(Collectors.toConcurrentMap(Person::getName,
                        Person::getAge));
        // threshold 越小越快
        String result = map.search(1,(k, v) ->v > 50? k: null );
        System.out.println("result >>> " + result);
        int sum = map.reduceValues(1,Integer::sum);
        System.out.println("sum >>>" + sum);
        long lsum = map.reduceValuesToLong(1,Long::valueOf,0,Long::sum);
        System.out.println("lsum >>>" + lsum);
        int keysLenSum = map.reduceKeys(2, String::length, Integer::sum);
        System.out.println("keysLenSum >>>" + keysLenSum);
    }

    /**
     * LongAdder(加法器) 适合做统计
     * 类似的有 {@link java.util.concurrent.atomic.LongAccumulator}
     */
    @Test
    void adder(){
        LongAdder adder = new LongAdder();
        adder.increment();
        adder.increment();
        assertEquals(adder.sum(),2);
        adder.increment();
        assertEquals("3",adder.toString());
        assertEquals(3,adder.sumThenReset());
        adder.add(12);
        adder.increment();
        assertEquals(13,adder.sum());

        LongAccumulator accumulator = new LongAccumulator(Long::sum,1);
        accumulator.accumulate(2);
        assertEquals(3,accumulator.get());
    }


}
class RandomPerson implements Supplier<Person>{

    // LongAdder adder = new LongAdder();
    long i = 1;
    Random r = new Random();
    @Override
    public Person get() {
        return new Person(i++,"user"+ i,10 +r.nextInt(90));
    }
}