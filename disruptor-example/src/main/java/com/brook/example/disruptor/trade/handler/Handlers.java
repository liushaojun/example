package com.brook.example.disruptor.trade.handler;

import com.brook.example.disruptor.trade.Trade;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import lombok.extern.log4j.Log4j2;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/7
 */
public interface Handlers {
    @Log4j2
    class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {

        @Override
        public void onEvent(Trade trade, long l, boolean b) throws Exception {
            this.onEvent(trade);
        }

        @Override
        public void onEvent(Trade trade) throws Exception {
            log.info("handler1: set name ");
            trade.setName("h1");
        }
    }

    @Log4j2
    class Handler2 implements EventHandler<Trade> {

        @Override
        public void onEvent(Trade trade, long l, boolean b) throws Exception {
            log.info("handler2: set price ");
            trade.setPrice(10.0);
        }

    }

    @Log4j2
    class Handler3 implements EventHandler<Trade> {

        @Override
        public void onEvent(Trade trade, long l, boolean b) throws Exception {

            log.info("handler3: name {}, price {},instance {}",
                    trade.getName(), trade.getPrice(), trade.toString());
        }

    }

    @Log4j2
    class Handler4 implements EventHandler<Trade>, WorkHandler<Trade> {

        @Override
        public void onEvent(Trade trade, long l, boolean b) throws Exception {
            this.onEvent(trade);
        }

        @Override
        public void onEvent(Trade trade) throws Exception {
            log.info("handler4: getName {}",
                    trade.getName());
            trade.setName(trade.getName() + "h4");
        }
    }

    @Log4j2
    class Handler5 implements EventHandler<Trade>, WorkHandler<Trade> {

        @Override
        public void onEvent(Trade trade, long l, boolean b) throws Exception {
            this.onEvent(trade);
        }

        @Override
        public void onEvent(Trade trade) throws Exception {
            log.info("handler5: getPrice {}",
                    trade.getPrice());
            trade.setPrice(trade.getPrice() + 3.0);
        }
    }

}
