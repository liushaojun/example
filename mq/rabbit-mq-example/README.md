## RabbitMQ Example
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
