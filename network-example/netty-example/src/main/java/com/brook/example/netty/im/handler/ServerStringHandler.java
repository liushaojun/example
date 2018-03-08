package com.brook.example.netty.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/6
 */
@Slf4j
public class ServerStringHandler extends SimpleChannelInboundHandler{

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端与服务端链接...");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端与服务端断开连接...");
  }
  @Override
  protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object o)
      throws Exception {
    log.info("server: {}", Objects.toString(o));
    channelHandlerContext.writeAndFlush(o.toString()+"您好");
  }
}
