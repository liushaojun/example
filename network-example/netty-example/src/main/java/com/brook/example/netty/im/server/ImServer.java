package com.brook.example.netty.im.server;

import com.brook.example.netty.im.handler.ServerMessageHandler;
import com.brook.example.netty.im.serial.MessageDecoder;
import com.brook.example.netty.im.serial.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/6
 */
public class ImServer {

  public static void main(String... args) {
    new Thread(() -> new ImServer().run(12345)).start();

  }
  public void run(int port){
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup,workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
               @Override
               protected void initChannel(SocketChannel ch) {
                 // 实体类
                 ch.pipeline().addLast("decoder",new MessageDecoder());
                 ch.pipeline().addLast("encoder",new MessageEncoder());
                 ch.pipeline().addLast(new ServerMessageHandler());
                 //                 字符串
//                 ch.pipeline().addLast("decoder",new StringDecoder());
//                 ch.pipeline().addLast("encoder",new StringEncoder());
//                 ch.pipeline().addLast(new ServerStringHandler());

               }
             })
        .option(ChannelOption.SO_BACKLOG,128)
        .childOption(ChannelOption.SO_KEEPALIVE,Boolean.TRUE);
    try {
      ChannelFuture future = bootstrap.bind(port).sync();
      future.channel().closeFuture().sync();
    }catch (InterruptedException e){
      //ignore
      Thread.currentThread().interrupt();
    }finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }

  }
}
