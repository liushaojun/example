package com.brook.example.disruptor;

import com.brook.example.disruptor.trade.Consumer;
import com.brook.example.disruptor.trade.Producer;
import com.brook.example.disruptor.trade.Trade;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
@Log4j2
public class ProducerConsumerTest {

    @Test
    public void test() throws InterruptedException {
        //创建ringBuffer
        RingBuffer<Trade> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                () -> new Trade(),
                1024 * 1024,
                new YieldingWaitStrategy());

        SequenceBarrier barrier = ringBuffer.newBarrier();

        Consumer[] consumers = new Consumer[3];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("c" + i);
        }

        WorkerPool<Trade> workerPool = new WorkerPool<>(ringBuffer,
                barrier,
                new IntEventExceptionHandler(),
                consumers);
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 10; i++) {
            final Producer p = new Producer(ringBuffer);
            new Thread(() -> {
                try {
                    // 等待生产
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < 10; j++) {
                    p.onData(UUID.randomUUID().toString());
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println("------------开始生产-------------");
        latch.countDown();
        Thread.sleep(5000);
        System.out.println("总数：" + consumers[0].getCount());

    }

    static class IntEventExceptionHandler implements ExceptionHandler {
        @Override
        public void handleEventException(Throwable arg0, long arg1, Object arg2) {
            log.info("error is {}", arg0.getMessage());
        }

        @Override
        public void handleOnShutdownException(Throwable arg0) {
            log.info("Shutdown error is {}", arg0.getMessage());

        }

        @Override
        public void handleOnStartException(Throwable arg0) {

            log.info("Start error is {}", arg0.getMessage());

        }
    }
}
