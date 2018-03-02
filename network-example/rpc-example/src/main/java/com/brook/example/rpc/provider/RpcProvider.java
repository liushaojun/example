package com.brook.example.rpc.provider;

import com.brook.example.rpc.DemoService;
import com.brook.example.rpc.RpcFramework;
import com.brook.example.rpc.impl.DemoServiceImpl;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */

public class RpcProvider {

  public static void main(String[] args) throws Exception {
    DemoService service = new DemoServiceImpl();
    RpcFramework.export(service, 12345);
  }
}
