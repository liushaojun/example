package com.brook.example.netty.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/6
 */
@Slf4j
public class ClientStringHandler extends SimpleChannelInboundHandler {

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
    log.info("client: {}",msg.toString());
  }

}
