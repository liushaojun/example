package com.brook.example.netty.im.handler;

import com.brook.example.netty.im.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/7
 */
@Slf4j
public class ClientMessageHandler extends SimpleChannelInboundHandler<Message>{

  @Override
  protected void messageReceived(ChannelHandlerContext ctx, Message msg) throws Exception {
    log.info("client: {}",msg.getContent());
  }
}
