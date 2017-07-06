package com.brook.example.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Sequence: Disruptor使用Sequence来表示一个特殊组件处理的序号。和Disruptor一样，每个消费者(EventProcessor)都维持着一个Sequence。
 *  大部分的并发代码依赖这些Sequence值的运转，因此Sequence支持多种当前为AtomicLong类的特性。
 *  <br/>
    Sequencer: 这是Disruptor真正的核心。实现了这个接口的两种生产者（单生产者和多生产者）均实现了所有的并发算法，为了在生产者和消费者之间进行准确快速的数据传递。
    <br/>
    SequenceBarrier: 由Sequencer生成，并且包含了已经发布的Sequence的引用，这些的Sequence源于Sequencer和一些独立的消费者的Sequence。它包含了决定是否有供消费者来消费的Event的逻辑。
    <br/>
   WaitStrategy：决定一个消费者将如何等待生产者将Event置入Disruptor。
    <br/>
    EventProcessor：主要事件循环，处理Disruptor中的Event，并且拥有消费者的Sequence。它有一个实现类是BatchEventProcessor，包含了event loop有效的实现，并且将回调到一个EventHandler接口的实现对象。
    <br/>
    EventHandler：由用户实现并且代表了Disruptor中的一个消费者的接口。
    <br/>
    Producer：由用户实现，它调用RingBuffer来插入事件(Event)，在Disruptor中没有相应的实现代码，由用户实现。
    <br/>
    WorkProcessor：确保每个sequence只被一个processor消费，在同一个WorkPool中的处理多个WorkProcessor不会消费同样的sequence。
    <br/>
    WorkerPool：一个WorkProcessor池，其中WorkProcessor将消费Sequence，所以任务可以在实现WorkHandler接口的worker吃间移交。
    <br/>
    LifecycleAware：当BatchEventProcessor启动和停止时，于实现这个接口用于接收通知。
 *
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/6
 */
public class LongEventTest {

    @Test
    public void longEvent() {
        //创建缓冲区
        ExecutorService executor = Executors.newCachedThreadPool();
        //创建工厂
        LongEventFactory factory = new LongEventFactory();
        //创建bufferSize，也就是RingBuffer的大小，必须是2的N次方
        int ringBufferSize = 1024 * 1024;

        /*//BlockingWaitStrategy是最低效的策略，但其对CPU的消耗最小并且在各种不同的环境中能够提供更加一致的性能表现
        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        //SleepingWaitStrategy的性能表现和BlockingWaitStrategy差不多，对CPU消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类的场景。
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        //YieldingWaitStrategy的性能最好，适用于低延迟的系统。在要求性能极高且时间处理线程数小于CPU逻辑线程数的场景中，推荐使用此策略：例如CPU开启超线程策略
        WaitStrategy YIELD_WAIT = new YieldingWaitStrategy();*/

        //创建Disruptor
        //1 第一个参数为工厂类对象
        //3 第三个参数是线程池，进行Disruptor内部的数据接受处理调度
        //4 第四个参数ProducerType.SINGLE和ProducerType.MULTI
        //5 第五个参数是一种策略:WaitStrategy
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, ringBufferSize, executor,
                ProducerType.SINGLE, new YieldingWaitStrategy());
        //连接消费事件方法
        disruptor.handleEventsWith(new LongEventHandler());
        //启动
        disruptor.start();

        //Disruptor的事件发布有两个阶段提交的过程
        //使用该方法获得具体存放数据的容器ringBuffer(环形结构)
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        // LongEventProducer producer = new LongEventProducer(ringBuffer);
             LongEventProducerWithTranslator producer = new LongEventProducerWithTranslator(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        for (long a = 0; a < 100; a++) {
            byteBuffer.putLong(0, a);
            producer.onData(byteBuffer);
        }

        disruptor.shutdown();//关闭Disruptor，方法会堵塞，知道所有的事件都得到处理
        executor.shutdown();//关闭Disruptor使用的线程池：如果需要的话，必须手动关闭，Disruptor在shutdown时不会自动关闭。

    }

}