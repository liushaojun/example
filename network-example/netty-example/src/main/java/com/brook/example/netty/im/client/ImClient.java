package com.brook.example.netty.im.client;

import com.brook.example.netty.im.handler.ClientMessageHandler;
import com.brook.example.netty.im.model.Message;
import com.brook.example.netty.im.serial.MessageDecoder;
import com.brook.example.netty.im.serial.MessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.UUID;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/6
 */
public class ImClient {

  private Channel channel;

  public static void main(String... args) {
    Channel ch = new ImClient().run("127.0.0.1",12345);
    //对象传输数据
    Message message = new Message();
    message.setId(UUID.randomUUID().toString().replaceAll("-", ""));
    message.setContent("你好，服务器!");
    ch.writeAndFlush(message);

  }
  public Channel run(String host, int port) {
    doRun(host, port);
    return this.channel;
  }

  private void doRun(String host, int port) {
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Bootstrap bootstrap = new Bootstrap();
    bootstrap.group(workerGroup)
        .channel(NioSocketChannel.class)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            // 实体类解析
            ch.pipeline().addLast("decoder", new MessageDecoder());
            ch.pipeline().addLast("encoder", new MessageEncoder());
            ch.pipeline().addLast(new ClientMessageHandler());
            // 字符串解析
//            ch.pipeline().addLast("decoder", new StringDecoder());
//            ch.pipeline().addLast("encoder", new StringEncoder());
//            ch.pipeline().addLast(new ClientStringHandler());
          }
        });
    ChannelFuture f = null;
    try {
      f = bootstrap.connect(host, port).sync();
      this.channel = f.channel();
    }catch (InterruptedException ignore) {
      Thread.currentThread().interrupt();
    }
  }

}
