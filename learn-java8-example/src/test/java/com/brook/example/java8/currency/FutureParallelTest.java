package com.brook.example.java8.currency;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertTimeout;

import com.brook.example.java8.junit.extra.tags.Slow;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/31
 */
public class FutureParallelTest {
    List<MyTask> tasks;

    @BeforeEach
    void initTasks() {
        tasks = IntStream.range(0, 10)
                .mapToObj(i -> new MyTask(1))
                .collect(toList());

    }

    @DisplayName("串行流")
    @Test
    @Slow
    void seq() {
        assertTimeout(Duration.ofSeconds(11), () -> MyTask.runSequentially(tasks));
    }

    @DisplayName("并行流")
    @Test
    void ps() {
        MyTask.useParallelStream(tasks);
    }

    @DisplayName("CompletableFuture 并行")
    @Test
    void cf() {
        MyTask.useCompletableFutureWithExecutor(tasks);
    }

    @DisplayName("CompletableFuture With Executor 并行")
    @Test
    void cfe() {
        MyTask.useCompletableFutureWithExecutor(tasks);
    }

}

class MyTask {
    private final int duration;

    public MyTask(int duration) {
        this.duration = duration;
    }

    public static void runSequentially(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<Integer> result = tasks.stream()
                .map(MyTask::calculate)
                .collect(toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }

    public static void useParallelStream(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<Integer> result = tasks.parallelStream()
                .map(MyTask::calculate)
                .collect(toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }

    public static void useCompletableFuture(List<MyTask> tasks) {
        long start = System.nanoTime();
        List<CompletableFuture<Integer>> futures =
                tasks.stream()
                        .map(t -> CompletableFuture.supplyAsync(() -> t.calculate()))
                        .collect(Collectors.toList());

        List<Integer> result =
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
    }

    public static void useCompletableFutureWithExecutor(List<MyTask> tasks) {
        long start = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(Math.min(tasks.size(), 10));
        List<CompletableFuture<Integer>> futures =
                tasks.stream()
                        .map(t -> CompletableFuture.supplyAsync(t::calculate, executor))
                        .collect(Collectors.toList());
        List<Integer> result =
                futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.printf("Processed %d tasks in %d millis\n", tasks.size(), duration);
        System.out.println(result);
        executor.shutdown();
    }

    public int calculate() {
        System.out.println(Thread.currentThread().getName());
        try {
            Thread.sleep(duration * 1000);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        return duration;
    }
}
