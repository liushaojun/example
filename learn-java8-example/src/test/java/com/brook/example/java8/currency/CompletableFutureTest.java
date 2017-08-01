package com.brook.example.java8.currency;

import com.google.common.collect.ImmutableMap;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
public class CompletableFutureTest {

    // 合并
    private static String merge(List<String> datas){
        return datas.parallelStream()
                .reduce((s, s2) -> {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return s + ";" + s2;

                }).orElse("");
    }

    /**
     * 流水式执行
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void sequentially() throws ExecutionException, InterruptedException {
        // 如果是I/O密集型,使用Executor 增加线程池大小
        // 如果是CPU密集型计算,就不能增加太多的计算使用 parallel stream 比较好
        ExecutorService executor = Executors.newFixedThreadPool(5);
        CompletableFuture<String> db = CompletableFuture.supplyAsync(()->this.getDataFromDB("db"),executor);
            db.thenApplyAsync(this::getDataFromRedis,executor)
                .thenAcceptAsync(this::handler,executor)
                // 异常捕获
                .exceptionally(e -> {
                    System.out.println("error :" + e.getMessage());
                    return null;
                })
                .whenComplete((aVoid, e) -> System.out.println("----> finish."))
                .get();
            executor.shutdown();

    }

    /**
     * 模拟从数据库获取数据
     * @return
     */
    public String getDataFromDB(String data){
        System.out.println("-------> get data from db.");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 并行执行
     * <p> (7s)
     *     db -> redis-> \
     *                    => merge(2s) => finish (9s)
     *     net      ->  /
     *    (6s)
     * </p>
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    void parallel() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        List<CompletableFuture<String>> futures = getFutures(executor);
        String result = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures
                .size()]))
                        .thenApplyAsync((v)-> futures.stream()
                                .map(CompletableFuture::join)
                                .collect(Collectors.toList()))
                        // 合并
                        .thenApplyAsync(CompletableFutureTest::merge,executor)
                        .exceptionally((e) -> {
                            System.out.println("error :" + e.getMessage());
                            return null;
                        })
                        .whenComplete(this::done)
                        .get();
         executor.shutdown();
        assertEquals("redis:data;net:data",result);
    }

    private List<CompletableFuture<String>> getFutures(Executor executor){
        CompletableFuture<String> dbFuture = CompletableFuture
                .supplyAsync(() -> this.getDataFromDB("db:data"),executor)
                .thenApplyAsync(this::getDataFromRedis,executor)
                .thenApplyAsync((map) -> map.values().toArray(new String[]{})[0]);

        CompletableFuture<String> netFuture = CompletableFuture
                .supplyAsync(this::getDataFromNet);
        return  Lists.newArrayList(dbFuture, netFuture);

    }

    void handler(Map<String,String> data){
        System.out.println("-------> 处理数据");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        data.forEach((k, v) -> System.out.printf("k = %s,v = %s \n\r",k,v));
    }

    private void done(String data,Throwable e){
            System.out.println("-----> " + data);
            System.out.println("----> " + "finish.");
    }

    /**
     * 模拟从redis 获取数据
     * @return
     */
    public Map<String, String> getDataFromRedis(String key){
        System.out.println("-------> get data from redis.");

        System.out.println("key is "+key );
        if(key == null){
            throw new RuntimeException(String.format("key [%s]不存在!",key));
        }
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ImmutableMap.of(key,"redis:data");
    }

    /**
     * 模拟从网络获取数据
     * @return
     */
    public String getDataFromNet(){
        System.out.println("-------> get data from net.");

        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "net:data";
    }
}
