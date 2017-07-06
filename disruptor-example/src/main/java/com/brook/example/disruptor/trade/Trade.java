package com.brook.example.disruptor.trade;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用 EventProcessor处理消息
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2017/7/6
 */
@Data
public class Trade {
    private String id;
    private String name;
    private double price;
    private AtomicInteger count = new AtomicInteger(0);
}

