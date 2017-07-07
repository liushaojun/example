package com.brook.example.disruptor.trade;

import com.lmax.disruptor.WorkHandler;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
@Log4j2
public class Consumer implements WorkHandler<Trade> {

private static AtomicInteger cnt = new AtomicInteger(0);
    private String consumerId;

        public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        log.info("Current consumer is {} \n " +
                " consumption's info is {} "
                , this.consumerId, trade.getId());
        cnt.incrementAndGet();
    }

    public int getCount(){
            return cnt.get();
    }
}
