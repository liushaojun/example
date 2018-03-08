package com.brook.example.netty.im.serial;

import com.brook.example.netty.im.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/7
 */
public class MessageDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if(in.capacity() == 0){
      return;
    }
    Object obj = ByteUtils.byteToObject(ByteUtils.read(in));
    out.add(obj);
  }
}
