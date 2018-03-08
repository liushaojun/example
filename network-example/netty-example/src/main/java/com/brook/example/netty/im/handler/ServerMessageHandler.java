package com.brook.example.netty.im.handler;

import com.brook.example.netty.im.model.Message;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/6
 */
@Slf4j
public class ServerMessageHandler extends SimpleChannelInboundHandler<Message>{

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端与服务端链接...");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端与服务端断开连接...");
  }
  @Override
  protected void messageReceived(ChannelHandlerContext channelHandlerContext, Message msg)
      throws Exception {

    log.info("server: messageId = {},content = {},",msg.getId(),msg.getContent());
    msg.setContent("hi,client.");
    channelHandlerContext.writeAndFlush(msg);
  }
}
