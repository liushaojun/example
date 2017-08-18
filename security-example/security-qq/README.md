### 准备工作

1. 在 [QQ互联](https://connect.qq.com/index.html) 申请成为开发者，并创建应用，得到APP ID 和 APP Key。

2. QQ接入开发文档 [网站应用接入流程](http://wiki.connect.qq
.com/%E7%BD%91%E7%AB%99%E5%BA%94%E7%94%A8%E6%8E%A5%E5%85%A5%E6%B5%81%E7%A8%8B)。（必须看完看懂）


### 运行应用
1、进入 security-qq 目录，执行：
```
mvn spring-boot:run
```
2、此处假设你已经修改好host，并启动成功，访问 http://127.0.0.1:8080/

3、登录 -> QQ登录 -> 个人中心，将会看到个人信息。



### 相关说明

> 腾讯官网原话：
openid是此网站上唯一对应用户身份的标识，网站可将此ID进行存储便于用户下次登录时辨识其身份，或将其与用户在网站上的原有账号进行绑定。

通过QQ登录获取的 openid 用于与自己网站的账号一一对应。

### 相关文章
- [Spring Security 入门：登录与退出](http://www.jianshu.com/p/a8e317e82425)
- [Spring Security 入门：自定义 Filter](http://www.jianshu.com/p/deb512b41f99)

### 相关资料
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture/)
- [What is authentication in Spring Security?](http://docs.spring.io/spring-security/site/docs/5.0.0.M2/reference/htmlsingle/#tech-intro-authentication)
