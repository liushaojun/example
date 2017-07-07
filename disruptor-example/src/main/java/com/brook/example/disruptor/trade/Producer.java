package com.brook.example.disruptor.trade;

import com.lmax.disruptor.RingBuffer;

import lombok.RequiredArgsConstructor;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
@RequiredArgsConstructor
public class Producer {

    private final RingBuffer<Trade> ringBuffer;

    /**
     * onData 用来发布事件,每调用一次就发布一次事件
     * 他的参数会通过事件传递给消费者
     * 
     * @param data
     */
    public void onData(String data) {
        long seq = ringBuffer.next();
        try {
            Trade trade = ringBuffer.get(seq);
            trade.setId(data);
        } finally {
            ringBuffer.publish(seq);
        }

    }
}
