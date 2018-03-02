package com.brook.example.rpc.consumer;

import com.brook.example.rpc.DemoService;
import com.brook.example.rpc.RpcFramework;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */

@Slf4j
public class RpcConsumer {


    public static void main(String[] args) throws Exception {
      DemoService service = RpcFramework.refer(DemoService.class, "127.0.0.1", 12345);
      for (int i = 0; i < 10; i++) {
        String hello = service.hello("World" + i);
        log.info(hello);
        Thread.sleep(1000);
      }
    }
}
