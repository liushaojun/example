# Disruptor 并发框架

> `Disruptor`是一个高性能的异步处理框架，或者可以认为是最快的消息框架(轻量的JMS)，也可以认为是一个观察者模式实现。
或者事件-监听模式的实现，直接称`disruptor`模式。`disruptor`最大特点是`高性能`。
如：其`LMAX`架构可以获得每秒6百万订单，用1微秒的延迟获得吞吐量为100K+。

## RingBuffer
> `RingBuffer`看作最主要的组件,然而3.0开始RingBuffer仅仅负责存储和更新在Disruptor中流通的数据。对于一些特殊的使用场景能够被用户(其他数据结构)完全替代.
`RingBuffer`他是一个首位相接的闭环,可以用它在不同上下文(线程)间传递数据的buffer


基本来说，RingBuffer 拥有一个序号，这个序号指向数组中下一个可用元素

![](http://i.imgur.com/nqzyhjh.png)

## RingBuffer 的优点

- 因为它是数组，所以要比链表快，而且有一个容易预测的访问模式。
- 这是对CPU缓存友好的，也就是说在硬件级别，数组中的元素是会被预加载的，因此在`RingBuffer`当中，cpu无需时不时去主存加载数组中的下一个元素。
- 你可以为数组预先分配内存，使得数组对象一直存在（除非程序终止）。这就意味着不需要花大量的时间用于垃圾回收。
此外，不像链表那样，需要为每一个添加到其上面的对象创造节点对象一一对应的，当删除节点时，需要执行相应的内存清理操作。


## 名词解释

### Sequence
`Disruptor`使用`Sequence`来表示一个特殊组件处理的序号。和`Disruptor`一样，每个消费者(`EventProcessor`)都维持着一个`Sequence`。
大部分的并发代码依赖这些`Sequence`值的运转，因此`Sequence`支持多种当前为`AtomicLong`类的特性。

### Sequencer
这是Disruptor真正的核心。实现了这个接口的两种生产者（单生产者和多生产者）均实现了所有的并发算法
，为了在生产者和消费者之间进行准确快速的数据传递。

### SequenceBarrier
由`Sequencer`生成，并且包含了已经发布的`Sequence`的引用，这些的`Sequence`源于`Sequencer`和一些独立的消费者的`Sequence`。
它包含了决定是否有供消费者来消费的`Event`的逻辑。

### WaitStrategy
`WaitStrategy` 决定一个消费者将如何等待生产者将Event置入`Disruptor`。

### EventHandler
`EventHandler` 由用户实现并且代表了`Disruptor`中的一个消费者的接口。
### EventProcessor

`EventProcessor` 主要事件循环，处理`Disruptor`中的`Event`，并且拥有消费者的`Sequence`。
它有一个实现类是`BatchEventProcessor`，包含了`event loop`有效的实现，并且将回调到一个`EventHandler`接口的实现对象。

### Producer
`Producer` 由用户实现，它调用`RingBuffer`来插入事件(`Event`)，在`Disruptor`中没有相应的实现代码，由用户实现。

### WorkProcessor
`WorkProcessor` 确保每个`sequence`只被一个`processor`消费，在同一个`WorkPool`中的处理多个`WorkProcessor`不会消费同样的`sequence`。

### WorkerPool
`WorkerPool` 一个`WorkProcessor`池，其中`WorkProcessor`将消费`Sequence`，所以任务可以在实现`WorkHandler`接口的`worker`之间移交。

### LifecycleAware
`LifecycleAware` 当`BatchEventProcessor`启动和停止时，于实现这个接口用于接收通知。


## 复杂场景应用

- 菱形

![](http://i.imgur.com/PtDPpWF.png)

- 六边形

![](http://i.imgur.com/jjK10Qd.png)

代码 TradeEventTest#test

## 生产者消费者模式

```java
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

```