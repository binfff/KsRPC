# `KsRPC`

​    	本项目是一个简易轻量级的RPC（远程过程调用）框架，基于spring-cloud。它提供了基本的服务注册与发现功能，简单的负载均衡，以及心跳检测。用于服务之间的相互调用。

## 项目结构

项目分为两个主要模块：

- **服务端模块**：负责服务注册与发现，以及心跳检测。基于tomcat实现
- **客户端模块**：衔接spring-cloud抽象层，提供服务消费功能，包括负载均衡和远程服务调用。该模块打包后供其他spring项目使用。

## 功能特性

- **服务注册与发现**：通过实现`ReactiveDiscoveryClient`和`AbstractAutoServiceRegistration`，实现与Spring Cloud生态系统的集成。
- **负载均衡**：提供简单的负载均衡机制，用于在多个服务实例之间分发请求。
- **服务间的调用**：提供了类似`@KsClient`的自定义注解，方便服务间的调用。

## 快速开始

由于自动配置功能更新，本项目需使用spring-boot 3.0以上

### 服务端模块

1. **克隆仓库**：

   `git clone https://github.com/binfff/KsRPC.git`

2. **使用Maven构建**：

   `mvn clean package`

3. **启动服务端**：

​	 `java -jar KsRpc-1.0-SNAPSHOT.jar`

### 客户端模块

1. **使用Maven构建**：
2. **在项目中引入客户端模块**：
3. **配置信息**

```yaml
ksrpc:
  registry:
  # 服务端地址信息
    url: http://127.0.0.1:8088
spring:
  application:
    name: kstalk-gateway
```

#### 网关服务引入

1.**启动类增加注解**`@EnableDiscoveryClient`

2.**引入负载均衡模块**

```
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-loadbalancer</artifactId>
</dependency>
```

## **服务间的相互调用**

### `@KsClient`

用于接口标记，使用JDK 动态代理机制，通过 `Proxy.newProxyInstance` 方法来动态生成接口代理对象。进行远程方法调用

使用方式：

```java
@KsClient(serviceName = "my-service")
public interface MyServiceClient {
    @GetMapping("/endpoint")
    String getData();
}
```

