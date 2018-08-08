package com.brook.example.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 简单 NIO 服务端聊天例子。可以通过 nc 进行连接测试
 * <pre>
 *   nc localhost 3000
 * </pre>
 *
 * @author Brook 😈
 * @since 2018/8/8
 */
public class NioServer {

  /**
   * 用于检测所有Channel状态的selector
   */
  private Selector selector;
  static final Charset CHARSET = StandardCharsets.UTF_8;

  private void start() throws IOException {
    selector = Selector.open();
    ServerSocketChannel server = ServerSocketChannel.open();
    int port = 3000;
    InetSocketAddress isa = new InetSocketAddress("127.0.0.1", port);
    server.bind(isa);
    System.out.println("Nio server listen on 127.0.0.1:"+port);
    server.configureBlocking(false);
    server.register(selector, SelectionKey.OP_ACCEPT);
    while (selector.select() > 0) {
      for (SelectionKey sk : selector.selectedKeys()) {
        // 从selector 上已经选择的key 集合中删除正在处理的 Selectionkey
        selector.selectedKeys().remove(sk);

        // 如果对应的Channel 已经包含了客户端连接
        if (sk.isAcceptable()) {
          final SocketChannel accept = server.accept();
          accept.configureBlocking(false);
          accept.register(selector, SelectionKey.OP_READ);
          // 将sk 对应的 Channel 设置成准备接受其他的请求
          sk.interestOps(SelectionKey.OP_ACCEPT);

        }
        if (sk.isReadable()) {
          final SocketChannel channel = (SocketChannel) sk.channel();
          final ByteBuffer buffer = ByteBuffer.allocate(1024);
          StringBuilder content = new StringBuilder();
          try {
            while (channel.read(buffer) > 0) {
              buffer.flip();
              content.append(CHARSET.decode(buffer));
            }
            System.out.println("读取的数据: " + content.toString());
            // 将sk对应的channel 设置成准备下一次读取
            sk.interestOps(SelectionKey.OP_READ);

          } catch (IOException e) {
            // 如果sk 对应的channel 出现了异常，即表明该 Channel 对应的Client 出现
            // 了问题，所以从selector 中取消 sk 注册
            sk.cancel();
            if (sk.channel() != null) {
              sk.channel().close();
            }
          }
          if (content.length() > 0) {
            for (SelectionKey key : selector.selectedKeys()) {
              final SelectableChannel target = key.channel();
              if (target instanceof SocketChannel) {
                SocketChannel cast = (SocketChannel) target;
                cast.write(CHARSET.encode(content.toString()));
              }
            }
          }
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new NioServer().start();
  }

}
