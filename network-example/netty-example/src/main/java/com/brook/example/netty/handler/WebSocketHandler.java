package com.brook.example.netty.handler;

import com.brook.example.netty.NettyConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<Object>{

  private WebSocketServerHandshaker handshaker;
  private static final String WS_URL = "ws://localhost:9000/websocket";

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    NettyConfig.GROUP.add(ctx.channel());
    log.info("客户端与服务端链接...");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    NettyConfig.GROUP.remove(ctx.channel());
    log.info("客户端与服务端断开连接...");
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    cause.printStackTrace();
    ctx.close();
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  // 处理客户端的WebSocket
  @Override
  protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object msg)
      throws Exception {
    if(msg instanceof FullHttpRequest){
      handHttpRequest(channelHandlerContext,(FullHttpRequest)msg);
    }else if(msg instanceof WebSocketFrame){
      handWebsocketFrame(channelHandlerContext,(WebSocketFrame)msg);
    }
  }

  /**
   * 处理客户端与服务端之前的websocket
   */
  private void handWebsocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
    if (frame instanceof CloseWebSocketFrame) {
      handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
    }
    //判断是否是ping消息
    if (frame instanceof PingWebSocketFrame) {
      ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
      return;
    }

    //判断是否是二进制消息，如果是二进制消息，抛出异常
    if (!(frame instanceof TextWebSocketFrame)) {
      log.warn("目前我们不支持二进制消息");
      throw new RuntimeException("【" + this.getClass().getName() + "】不支持消息");
    }
    //返回应答消息
    //获取客户端向服务端发送的消息
    String request = ((TextWebSocketFrame) frame).text();
    log.info("服务端收到客户端的消息====>>>" + request);
    TextWebSocketFrame tws = new TextWebSocketFrame(LocalDateTime.now().toString()
        + ctx.channel().id()
        + " ===>>> "
        + request);
    //群发
    NettyConfig.GROUP.writeAndFlush(tws);
  }


  /**
   * 处理客户端向服务端发起http握手请求的业务
   */
  private void handHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
    if (!req.getDecoderResult().isSuccess()
        || !("websocket".equals(req.headers().get("Upgrade")))) {
      sendHttpResponse(ctx, req,
          new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
      return;
    }
    WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
        WS_URL, null, false);
    handshaker = wsFactory.newHandshaker(req);
    if (handshaker == null) {
      WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
    } else {
      // 同时将WebSocket相关的编码和解码类动态添加到ChannelPipeline中，用于WebSocket消息的编解码，
      // 添加WebSocketEncoder和WebSocketDecoder之后，服务端就可以自动对WebSocket消息进行编解码了
      handshaker.handshake(ctx.channel(), req);
    }
  }

  /**
   * 服务端向客户端响应消息
   */
  private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req,
      DefaultFullHttpResponse res) {
    if (res.getStatus().code() != HttpResponseStatus.OK.code()) {
      ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
      res.content().writeBytes(buf);
      buf.release();
    }
    //服务端向客户端发送数据
    ChannelFuture f = ctx.channel().writeAndFlush(res);
    // 如果是非Keep-Alive，关闭连接
    if (res.getStatus().code() != HttpResponseStatus.OK.code()) {
      f.addListener(ChannelFutureListener.CLOSE);
    }
  }
}
