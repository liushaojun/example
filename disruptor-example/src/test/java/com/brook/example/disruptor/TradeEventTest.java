package com.brook.example.disruptor;

import com.brook.example.disruptor.trade.Trade;
import com.brook.example.disruptor.trade.TradeHandler;
import com.brook.example.disruptor.trade.handler.Handlers;
import com.brook.example.disruptor.trade.handler.TradePublisher;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.concurrent.*;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/6
 */
@Log4j2
public class TradeEventTest {
    static final int BUFFER_SIZE = 1024;
    static final int THREAD_NUMBERS = 4;
    @Test
    public void process() throws ExecutionException, InterruptedException {

        /**
         * createSingleProducer创建一个单例生产者的RingBuffer。
         * 第一个参数交EventFactory。事件工厂，负责产生数据填充的RingBuffer的区块
         *
         * 第三个参数是RingBuffer的生产者在没有可用区块的时候
         * (可能是消费者（或者说是事件处理器）太慢了)的等待策略。
         */

        final RingBuffer<Trade> ringBuffer = RingBuffer
                .createSingleProducer(() -> new Trade(), BUFFER_SIZE, new YieldingWaitStrategy());

        //创建线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);
        //创建SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        //创建消息处理器
        BatchEventProcessor<Trade> transProcessor = new BatchEventProcessor<>(ringBuffer,
                sequenceBarrier, new TradeHandler());

        //把消费者的位置信息引用注入到生产者 如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(transProcessor.getSequence());
        //把消息处理器提交到线程池
        executors.submit(transProcessor);

        //执行生产者的工作
        //如果存在多个消费者 那重复执行上面的三行代码 把TradeHandler换成其他消费者
        Future<?> future = executors.submit((Callable<Void>) () -> {
            long seq;
            for (int i = 0; i < 10; i++) {
                seq = ringBuffer.next();//
                ringBuffer.get(seq).setPrice(Math.random() * 9999);//给这个区块放入数据
                ringBuffer.publish(seq);//发布这个区块的数据使handler（consumer）可见
            }
            return null;
        });

        future.get();
        Thread.sleep(1000);
        //通知事件 或者说处理器可以结束了
        transProcessor.halt();
        executors.shutdown();
    }

    @Test
    public void  workPool() throws InterruptedException {

        EventFactory<Trade> eventFactory = () -> new Trade();
        RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBERS);

        WorkHandler<Trade> handler = new TradeHandler();
        WorkerPool<Trade> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), handler);
        workerPool.start(executor);

        //下面这个生产8个数据
        for (int i = 0; i < 8; i++) {
            long seq = ringBuffer.next();
            ringBuffer.get(seq).setPrice(Math.random() * 9999);
            ringBuffer.publish(seq);
        }

        Thread.sleep(1000);
        workerPool.halt();
        executor.shutdown();
    }

    @Test
    public void test() throws InterruptedException {
        long beginTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(8);

        Disruptor<Trade> disruptor = new Disruptor<>(() -> new Trade(), BUFFER_SIZE, executor, ProducerType.SINGLE, new BusySpinWaitStrategy());

        //菱形操作
        //使用disruptor创建消费者组C1和C2
        /*EventHandlerGroup<Trade> handlerGroup =
                disruptor.handleEventsWith(new Handler1(),new Handler2());
           handlerGroup.then(new Handler3());
        */

        //顺序操作
        /*disruptor.handleEventsWith(new Handler1()).
            handleEventsWith(new Handler2()).
            handleEventsWith(new Handler3());*/

        //六边形操作

        Handlers.Handler1 h1 = new Handlers.Handler1();
        Handlers.Handler2 h2 = new Handlers.Handler2();
        Handlers.Handler3 h3 = new Handlers.Handler3();
        Handlers.Handler4 h4 = new Handlers.Handler4();
        Handlers.Handler5 h5 = new Handlers.Handler5();
        disruptor.handleEventsWith(h1, h2);
        disruptor.after(h1).handleEventsWith(h4);
        disruptor.after(h2).handleEventsWith(h5);
        disruptor.after(h4, h5).handleEventsWith(h3);

        disruptor.start();//启动
        CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        executor.submit(new TradePublisher(latch, disruptor));
        //等待生产者完事
        latch.await();
        disruptor.shutdown();
        executor.shutdown();
       log.info("总耗时：" + (System.currentTimeMillis() - beginTime));
    }


}
