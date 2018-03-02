## Netty 实现简单WebSocket
### 什么是netty
> netty 是封装JDK nio 提供高性能、高可靠性的异步IO框架。同类产品Mina、Grizzly。

1. 本质：JBoss做的一个Jar包
2. 目的：快速开发高性能、高可靠性的网络服务器和客户端程序
3. 优点：提供异步的、事件驱动的网络应用程序框架和工具

### 优势
API简单，性能高，入门门槛低，成熟稳健，修复了很多原生NIO的bug。


### 简单WebSocket
- 运行`NettyServer#main`方法启动，`WebSocket`服务。
- 访问 `index.html` 进行测试。