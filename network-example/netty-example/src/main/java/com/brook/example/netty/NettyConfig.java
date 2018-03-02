package com.brook.example.netty;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @author Shaojun Liu <liushaojun@maizijf.com>
 * @create 2018/3/2
 */
public final class NettyConfig {

  /**
   * 存储每一个客户端接入进来时的channel对象
   */
  public static ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
