## RabbitMQ Example
>`RabbitMQ` 是消息中间件的一种, 消息中间件即分布式系统中完成消息的发送和接收的基础软件.
其他有`ActiveMQ`、阿里高性能`RocketMQ`及性能极高的`Kafka`.

### 工作原理
`RabbitMQ` 来说, 除了`生产者`、`消息队列`、`消费者`三个基本模块以外,还有更重要的`交换机(Exchange)`.
它使得生产者和消息队列之间产生了隔离,生产者将消息发送给交换机,而交换机则根据`调度策略`把相应的消息转发给对
应的消息队列。交换机主要有四种类型,如下
- Direct 默认交换模式。
即创建消息队列的时候,指定一个 `BindingKey`。 当发送者发送消息的时候, 指定对应的 `Key`. 当 `Key` 和消息队列的
`BindingKey` 一致的时候,消息将会被发送到该消息队列中。
- Topic
转发信息主要是依据通配符, 队列和交换机的绑定主要是依据一种模式(通配符+字符串), 而当发送消息的时候,
只有指定的 Key 和该模式相匹配的时候, 消息才会被发送到该消息队列中.
- Fanout
是路由广播的形式, 将会把消息发给绑定它的全部队列, 即便设置了 `key`, 也会被忽略.

- Headers
根据一个规则进行匹配, 在消息队列和交换机绑定的时候会指定一组键值对规则, 而发送消息的时候也会指定一组键值对规则,
当两组键值对规则相匹配的时候, 消息会被发送到匹配的消息队列中.



![](https://ws2.sinaimg.cn/large/006tNc79ly1fnuk4ohjbmj30dd08zjrw.jpg)
### 安装rabbitMQ
#### Docker 安装方式
```bash
docker run -d -p 15672:15672 -p  5672:5672  -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin --name rabbitmq rabbitmq:3-management
```
- RabbitMQ api 文档 `http://localhost:15672/api/`
- 15672 RabbitMQ 控制台端口号，可以在浏览器进行管理。
- 5672 RabbitMQ 监听的TCP端口号
- RABBITMQ_DEFAULT_USER：用户名 admin
- RABBITMQ_DEFAULT_PASS：密码 admin
