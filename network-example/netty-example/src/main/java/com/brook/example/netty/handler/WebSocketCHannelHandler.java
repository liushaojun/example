package com.brook.example.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */
public class WebSocketCHannelHandler extends ChannelInitializer<SocketChannel>{

  @Override
  protected void initChannel(SocketChannel channel) throws Exception {
    channel.pipeline().addLast("http-codec",new HttpServerCodec());
    channel.pipeline().addLast("aggregator",new HttpObjectAggregator(Character.MAX_VALUE));
    channel.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
    channel.pipeline().addLast("handler", new WebSocketHandler());
  }
}
