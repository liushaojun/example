package com.brook.example.disruptor.trade.handler;

import com.brook.example.disruptor.trade.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.log4j.Log4j2;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class TradePublisher implements Runnable {

    Disruptor<Trade> disruptor;
    private CountDownLatch latch;

    private static int LOOP = 10;// 模拟百万次交易

    public TradePublisher(CountDownLatch latch,Disruptor<Trade> disruptor) {
        this.disruptor=disruptor;
        this.latch=latch;
    }

    @Override
    public void run() {
        TradeEventTranslator tradeTranslator = new TradeEventTranslator();
        for(int i=0;i<LOOP;i++){
            disruptor.publishEvent(tradeTranslator);
        }
        latch.countDown();
    }
}

class TradeEventTranslator implements EventTranslator<Trade> {

    private Random random = new Random();

    @Override
    public void translateTo(Trade event, long sequence) {
        this.generateTrade(event);
    }

    private Trade generateTrade(Trade trade) {
        trade.setPrice(random.nextDouble() * 9999);
        return trade;
    }

}
