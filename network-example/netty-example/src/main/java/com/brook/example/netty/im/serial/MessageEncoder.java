package com.brook.example.netty.im.serial;

import com.brook.example.netty.im.model.Message;
import com.brook.example.netty.im.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/7
 */
public class MessageEncoder extends MessageToByteEncoder<Message>{


  @Override
  protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
    byte [] bytes = ByteUtils.objectToByte(msg);
    out.writeBytes(bytes);
    ctx.flush();
  }
}