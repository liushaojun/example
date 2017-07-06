package com.brook.example.disruptor;

import com.lmax.disruptor.RingBuffer;
import lombok.RequiredArgsConstructor;

import java.nio.ByteBuffer;

/**
 * 当用一个简单的队列来发布时间的时候会牵涉更多的细节，这是因为事件对象还需要预先创建。发布事件最少需要
 * 两步：获取一个事件并发布事件(发布事件的时候使用try{}finally 保证事件一定发布)
 * 如果我们使用RingBuffer.next()获取一个事件槽，那么一定要发布对应的事件。
 * 如果不能发布事件，那么会引起Disruptor状态的<strong>混乱</strong>。
 * 尤其是在多个事件生产者的情况下会导致事件消费者失速，从而不得不重启应用才能恢复。
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/6
 */
@RequiredArgsConstructor
public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;


    /**
     * onData用来发布事件，每调用一次就发布一次事件
     * 它的参数会通过事件传递给消费者
     *
     * @param bb
     */
    public void onData(ByteBuffer bb){

        //1 可以把ringBuffer看作一个事件队列那么next就是得到下一个事件槽
        long seq = ringBuffer.next();
        try {
            // 获取序号对应的事件对象
            LongEvent longEvent = ringBuffer.get(seq);
            //3 获取要通过事件传递的业务数据
            longEvent.setValue(bb.getLong(0));
        } finally {
            // 发布事件
            // 注意：最后的ringBuffer#publish 方法必须包含finally 中确保必须得到调用
            ringBuffer.publish(seq);
        }

    }
}
