## NATS
> `NATS`是一个开源的、轻量级的、高性能的分布式消息通信系统，实现了高可伸缩性和优雅的发布/订阅模型。
`NATS`适合云基础设施的消息通信系统、`IoT`设备消息通信和微服务架构。
`Apcera`、`百度` 等公司,`西门子`,`VMware`,`HTC`,`爱立信` 依靠 `NATS` 以其高性能和弹性的消息传递功能。

### NATS 性能
各消息中间件的性能对比：

![](http://img.blog.csdn.net/20160328111343556)

### 设计目标
- 高性能
- 一直可用
- 极度轻量级
- 最多交付一次
- 支持多种消息通信模型 和 用例

### 应用场景
-  寻址、发现
- 命令 和 控制
- 负载均衡
- 多路可伸缩
- 定位透明性
- 容错
### 消息模型
应用程序的数据被编码为一条消息，并通过发布者发送出去。
订阅者接收到消息，进行解码，再处理。订阅者处理 `NATS` 消息可以是`同步` 或 `异步`的。
![](https://www.nats.io/img/documentation/nats-msg.png)
1. 发布、订阅 模型
![](https://www.nats.io/img/documentation/nats-pub-sub.png)
`NATS` 订阅 是一个一对多的消息通信。发布者在一个主题上发送消息，任何注册(订阅)了此主题消息的都可以
接收该消息。订阅者可以使用通配符进行注册感兴趣的主题。
如果客户端没有注册某个主题（或者客户端不在线），那么该主题发布消息时，客户端不会收到该消息。
`NATS` 系统是一种 `发送后不管` 的消息通信系统，故如果需要高级服务，需要在客户端开发相应的功能。
对于异步消息通信，消息交付给订阅者的消息句柄。如果客户端没有句柄，那么该消息通信是同步的，
那么客户端可能会被阻塞，直到它处理了该消息

2. 请求、响应 模型
![](https://www.nats.io/img/documentation/nats-req-rep.png)
`NATS` 支持两种请求-响应消息通信：`P2P`（点对点）和 `O2M`（一对多）。

  `P2P` 最快、响应也最先。而对于O2M，需要设置请求者可以接收到的响应数量界限。
在 请求-响应 的消息交换，发布请求操作会发布一个带预期响应的消息到 `Reply` 主题。
请求创建了一个收件箱，并在收件箱执行调用，并进行响应和返回。

3. 队列 模型
![](https://www.nats.io/img/documentation/nats-queue.png)
`NATS` 支持使用点对点消息排队(一对一)通信。

  要创建一个消息队列，订阅者需注册一个`队列名`。所有的订阅者用同一个队列名，形成一个队列组。
当消息发送到主题后，队列组会自动选择一个成员接收消息。尽管队列组有多个订阅者，
但每条消息只能被组中的一个订阅者接收。
队列的订阅者可以是异步的，这意味着消息句柄以回调方式处理交付的消息。异步队列订阅者必须建立处理消息的逻辑。

### 特性
- 纯发布、订阅
- 集群
- 订阅者自动修剪
- 基于文本协议


### 安装
- Mac 环境
`brew install gnatsd`
- Linux 环境
可以采用二进制包 和 `Docker` 镜像进行安装。
`wget https://github.com/nats-io/gnatsd/releases/download/v1.0.4/gnatsd-v1.0.4-linux-386.zip`

- Docker
`docker run -d --name nats-main nats`
[更多参数](https://hub.docker.com/_/nats/)

- Windows 环境
`choco install gnatsd`

### 配置与部署
#### 服务器选项
- `-a`， –addr HOST 绑定主机IP地址（默认是0.0.0.0）
- `-p`， –port PORT 客户端连接NATS服务器使用的端口（默认是4222）
- `-P`， –pid FILE 存储PID的文件
- `-m`， –http_port PORT 使用HTTP端口作为监听端口
- `-ms`， –https_port PORT 使用HTTPS端口作为监听端口
- `-c`， –config FILE 指定配置文件
#### 日志选项
- `-l`， –log FILE 指定日志输出的文件
- `-T`， –logtime 是否开启日志的时间戳（默认为true）
- `-s`， –syslog 启用syslog作为日志方法
- `-r`， –remote_syslog 远程日志服务器的地址（默认为udp://localhost:514）
- `-D`， –debug 开启调试输出
- `-V`, –trace 跟踪原始的协议
- `-DV` 调试并跟踪
#### 认证
- `–user` user 连接需要的用户名
- `–pass` password 连接需要的密码
#### TLS安全选项
-  `–tls` 启用TLS，不验证客户端（默认为false）
-  `–tlscert` FILE 服务器证书文件
-  `–tlskey` FILE 服务器证书私钥
-  `–tlsverify` 启用TLS，每一个客户端都要认证
-  `–tlscacert` FILE 客户端证书CA用于认证
#### 集群
- `–routes` [rurl-1, rurl-2] 路线征求并连接
```
gnatsd  -m 8222  -cluster nats://127.0.0.1:5222
gnatsd  -p 4223 -m 8223 -cluster nats://127.0.0.1:5223 -routes nats://127.0.0.1:5222
```