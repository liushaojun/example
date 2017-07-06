package com.brook.example.disruptor.trade;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;

@Log4j2
public class TradeHandler implements EventHandler<Trade>, WorkHandler<Trade> {

    @Override
    public void onEvent(Trade trade, long l, boolean b) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        //这里做具体的消费逻辑
        trade.setId(UUID.randomUUID().toString());//生成UUID
        log.info("id: " + trade.getId() + "|  price:");
        log.info(trade.getPrice());
    }
}