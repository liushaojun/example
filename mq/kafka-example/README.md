## Kafka
>`Apache Kafka`是分布式发布-订阅消息系统。它最初由`LinkedIn`公司开发，之后成为`Apache`项目的一部分。
`Kafka`是一种快速、可扩展的、设计内在就是分布式的，分区的和可复制的提交日志服务。
[官网](http://kafka.apache.org/)

![](https://images2017.cnblogs.com/blog/760273/201711/760273-20171108181426763-1692750478.png)

### Kafka 优势
- 分布式、易扩展
- 高吞吐量
- 负载均衡
- 可持久化

### 应用场景
1. 在系统与应用之间建立实时数据通道，消息队列功能。
2. 构建实时数据处理流,数据处理功能。


### 安装
#### Mac 系统
`brew install kafka`
#### Linux 系统
1. 下载
`wget http://mirrors.shuosc.org/apache/kafka/1.0.0/kafka_2.11-1.0.0.tgz`

2. 解压
`tar -zxf kafka_2.11-1.0.0.tgz && cd /usr/local/kafka_2.11-1.0.0/`
3. 配置
```bash
vi /usr/local/kafka/config/server.properties
# 修改
broker.id=1
log.dir=/data/kafka/logs-1
```
### 启动
1. 启动zk
`bin/zookeeper-server-start.sh -daemon config/zookeeper.properties`
2. 启动kafka
`bin/kafka-server-start.sh  config/server.properties`

### 操作Topic
- 创建
```
./kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic
test
```
- 查询
`./kafka-topics.sh --list --zookeeper localhost:2181`
如遇[https://stackoverflow.com/questions/28109669/zookeeper-unable-to-open-socket-to-localhost-000000012181](https://stackoverflow.com/questions/28109669/zookeeper-unable-to-open-socket-to-localhost-000000012181)
这个错误可以修改`/etc/hosts`文件
```bash
127.0.0.1   localhost localhost.localdomain localhost4 localhost4.localdomain4
::1         ip6-localhost ip6-localhost.localdomain localhost6 localhost6.localdomain6
```
- 生产消息
`./kafka-console-producer.sh --broker-list localhost:9092 --topic test`

- 消费消息
`./kafka-console-consumer.sh --zookeeper localhost:2181 --topic test --from-beginning`
- 查看Topics 信息
`./kafka-topics.sh --describe --zookeeper localhost:2181 --topic test`
第一行列出所有分区的摘要信息。
`Leader` 是负责给定分区的所有读取和写入的节点。每个节点成为分区随机选择的部分的领导者。
`Replicas` 是负责此分区日志的节点列表。无论是否领导状态，或当前处于活动状态。
`lsr` `同步`副本。复制品列表的子集,当前活着并被引导到领导者。

### 集群配置
`Kafka` 支持两种模式的集群搭建：可以在单机上运行多个 `broker` 实例来实现集群，也可在多台机器上搭建集群，
下面介绍下如何实现单机多 `broker` 实例集群，其实很简单，只需要如下配置即可。

#### 单机多 Broker 集群
###### 配置

```bash
cp config/server.properties config/server-2.properties
cp config/server.properties config/server-3.properties
vim config/server-2.properties
vim config/server-3.properties

```
修改为
```bash
# 2
broker.id=2
listeners = PLAINTEXT://your.host.name:9093
log.dir=/data/kafka/logs-2
# 3
broker.id=3
listeners = PLAINTEXT://your.host.name:9094
log.dir=/data/kafka/logs-3
```
##### 启动
```
bin/kafka-server-start.sh config/server-2.properties &
bin/kafka-server-start.sh config/server-3.properties &
```
#### 多机多 Broker 集群配置
分别在三台机器安装`kafka`实例,并配置多个`Zookeeper` 实例。
分别配置多个机器上的 Kafka 服务，设置不同的 Broker id, zookeeper.connect 设置如下:
`vi config/server.properties` 的 `zookeeper.connect`
修改为
`zookeeper.connect=192.168.1.1:2181,192.168.1.2:2181,192.168.1.3:2181`

### 导入/导出 数据
从控制台写入数据并将其写回控制台是一个方便的起点，但您可能想要使用其他来源的数据或将数据从 `Kafka` 导出到其他系统。
对于许多系统，您可以使用 `Kafka Connect` 来导入或导出数据，而不必编写自定义集成代码。
`Kafka Connect`有两个核心概念`Source`和`Sink` 负责导入和导出。
![](http://img.blog.csdn.net/20170226155201976?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdTAxMTY4NzAzNw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)
#### 概念
- Connectors 通过管理任务来细分数据流高级抽象
- Tasks 数据写入kafka和数据从kafka读出实现
- Workers 运行`connectors`和`tasks`的进程
- Converters `kafka connect`和其他存储系统直接发送或者接受数据之间转换数据

- 单体 `bin/connect-standalone.sh config/connect-standalone.properties connector1.properties
[connector2
.properties ...]`
- 分布式 `bin/connect-distributed.shconfig/connect-distributed.properties `
#### 测试推送文件数据
- 配置connect-file-source.properties
```
# cat connect-file-source.properties
name=local-file-source
connector.class=FileStreamSource
tasks.max=1
file=test.txt
topic=connect-test

```
`topic`的偏移量存储在`/tmp/connect.offsets`这个文件中,在`config/connect-standalone.properties`配置,
每次`connect`启动的时候会根据`connector`的`name`获得`topic`偏移量,然后在继续读取或者写入数据。

- 启动 Kafka connect
`bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties`
- 查看推送到`topic connect-test` 的数据
`bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic connect-test --from-beginning`
- 修改`connect-file-source.properties` 对数据进行转换,配置如下
```
name=local-file-source
connector.class=FileStreamSource
tasks.max=1
file=test.txt
topic=connect-test

transforms=MakeMap, InsertSource
transforms.MakeMap.type=org.apache.kafka.connect.transforms.HoistField$Value
transforms.MakeMap.field=line
transforms.InsertSource.type=org.apache.kafka.connect.transforms.InsertField$Value
transforms.InsertSource.static.field=data_source
transforms.InsertSource.static.value=test-file-source
```
- 启动Kafka connect
`bin/connect-standalone.sh config/connect-standalone.properties=config/connect-file-source.properties`
#### 将Topic connect-test 的数据保存到文件中
- 配置cat config/connect-file-sink.properties
```
name=local-file-sink
connector.class=FileStreamSink
tasks.max=1
file=test.sink.txt
```
- 启动Kafka connect
`bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-sink.properties`

- 启动多个connector
`bin/connect-standalone.sh config/connect-standalone.properties config/connect-file-source.properties config/connect-file-sink.properties`

### 使用 Kafka 流来处理数据
`Kafka Streams` 是用于构建关键任务实时应用程序和微服务的客户端库，输入或输出数据存储在 `Kafka` 集群中。
`Kafka Streams` 结合了在客户端编写和部署标准 `Java` 和 `Scala` 应用程序的简单性以及 `Kafka` 服务器端集群技术的优势，
使这些应用程序具有高度可伸缩性，弹性，容错性，分布式等特性。
[官网文档](http://kafka.apache.org/10/documentation/streams/quickstart)

