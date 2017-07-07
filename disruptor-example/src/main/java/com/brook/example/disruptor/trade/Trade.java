package com.brook.example.disruptor.trade;

import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * Trade 模型
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

