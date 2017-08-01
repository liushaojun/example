package com.brook.example.java8.stream.forker;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * 复制流
 * <p>
 * {@code StreamForker} 在一个流上执行多个操作。
 * 因为Java 8中，流有一个非常大的(也可能是最大的)局限性，使用时，对它操作一次仅能得到
 * 一个处理结果。
 */
public class StreamForker<T> {

    private final Stream<T> stream;
    private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

    public StreamForker(Stream<T> stream) {
        this.stream = stream;
    }


    public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
        forks.put(key, f);
        return this;
    }

    public Results getResults() {
        ForkingStreamConsumer<T> consumer = build();
        try {
            stream.sequential().forEach(consumer);
        } finally {
            consumer.finish();
        }
        return consumer;
    }

    /**
     * 构建一个消费流，把结果放到阻塞队列中
     * @return
     */
    private ForkingStreamConsumer<T> build() {
        List<BlockingQueue<T>> queues = new ArrayList<>();

        Map<Object, Future<?>> actions =
                forks.entrySet().stream().reduce(new HashMap<Object, Future<?>>(),
                        (map, e) -> {
                            map.put(e.getKey(),
                                    getOperationResult(queues, e.getValue()));
                            return map;
                        },
                        (m1, m2) -> {
                            m1.putAll(m2); // 经个人测试这的代码没有效果
                            return m1;
                        });

        return new ForkingStreamConsumer<>(queues, actions);
    }

    /**
     * 创建一个新的{@code BlockingQueue}，并将其添加到队列的列表。 这个队列会被传递给一个
     * 新的{@code BlockingQueueSpliterator }对象，后者是一个延迟绑定的 {@code Spliterator}，
     * 它会遍历读取队列中的每个元素;
     * @param queues
     * @param f
     * @return
     */
    private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
        BlockingQueue<T> queue = new LinkedBlockingQueue<>();
        queues.add(queue);
        Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
        Stream<T> source = StreamSupport.stream(spliterator, false);
        return CompletableFuture.supplyAsync(() -> f.apply(source));
    }

    public interface Results {
        <R> R get(Object key);
    }

    private static class ForkingStreamConsumer<T> implements Consumer<T>, Results {
        static final Object END_OF_STREAM = new Object();

        private final List<BlockingQueue<T>> queues;
        private final Map<Object, Future<?>> actions;

        ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
            this.queues = queues;
            this.actions = actions;
        }

        @Override
        public void accept(T t) {
            queues.forEach(q -> q.add(t));
        }

        @Override
        public <R> R get(Object key) {
            try {
                return ((Future<R>) actions.get(key)).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        void finish() {
            accept((T) END_OF_STREAM);
        }
    }

    private static class BlockingQueueSpliterator<T> implements Spliterator<T> {
        private final BlockingQueue<T> q;

        BlockingQueueSpliterator(BlockingQueue<T> q) {
            this.q = q;
        }

        /**
         *
         * @implNote 依据 {@link #getOperationResult}方法创建 {@link Spliterator }
         * 同样的方式,这些元素会被作为进一步处理流的源头传递给Consumer对象 (在流上要执行的
         * 函数会作为参数传递给某个fork方法调用)。
         * {@link #tryAdvance(Consumer)} 方法返回<code>true</code>通知调用方还有其他
         * 的元素需要处理，直到它发现由 {@code ForkingSteamConsumer} 添加的特殊对象
         * ,表明队列中已经没有更多需要处理的元素了。
         * @param action
         * @return boolean
         */
        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            T t;
            while (true) {
                try {
                    t = q.take();
                    break;
                } catch (InterruptedException e) {
                }
            }

            if (t != ForkingStreamConsumer.END_OF_STREAM) {
                action.accept(t);
                return true;
            }

            return false;
        }

        /**
         * 由于无法预测能从队列中取得多少个元素，所以{@code estimatedSize}方法也无法返回
         * 任何有意 义的值。更进一步，由于你没有试图进行任何切分，所以这时的估算也没什么用处
         *
         * @return
         */
        @Override
        public Spliterator<T> trySplit() {
            return null;
        }

        @Override
        public long estimateSize() {
            return 0;
        }

        @Override
        public int characteristics() {
            return 0;
        }
    }
}
