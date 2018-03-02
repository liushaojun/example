package com.brook.example.rpc.impl;

import com.brook.example.rpc.DemoService;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */
public class DemoServiceImpl implements DemoService {

  @Override
  public String hello(String name) {
    return "hello "+ name;
  }
}
