# Spring Security From Zero To One

## 模块说明

### [unsecured-web-application](unsecured-web-application)

简单的无安全保护的Web应用程序，作为对比示例。

参考 https://spring.io/guides/gs/securing-web Create an Unsecured Web Application

### [secured-web-application](secured-web-application)

基于模块 unsecured-web-application，使用Spring Security对其进行安全保护，展示基本的认证和授权功能。

参考 https://spring.io/guides/gs/securing-web Set up Spring Security

登录信息

- 用户名：user
- 密码：password

### [hello-spring-security](hello-spring-security)

Spring Web简单集成Spring Security项目，仅提供一个`/hello`端点。

默认登录信息

* 用户名：user
* 密码：服务启动时自动生成（Using generated security password）

```shell
# 无认证信息
curl --url 'http://localhost:8080/hello'
   
# 带认证信息
curl --url 'http://localhost:8080/hello' --user 'user:<generated-password-from-console>'

# 通过发送Basic Auth请求头的方式访问
# 1. 先将"user:password"进行Base64编码
echo -n 'user:<generated-password-from-console>' | base64
# 2. 然后使用编码后的字符串进行访问
curl --url 'http://localhost:8080/hello' --header 'Authorization: Basic <base64-encoded-credentials>'
```

### [custom-sign-up-and-sign-in](custom-sign-up-and-sign-in)

#### 技术栈

- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- H2 Database
- BCryptPassword Encoder

#### 功能

- 实现用户注册和登录功能
- 使用 Spring Security 进行安全保护
- 使用 Thymeleaf 渲染前端页面
- 使用 H2 内存数据库存储用户信息
- 密码加密存储
- 基本的表单验证
- 登录后重定向到欢迎页面
- 注册后自动登录
- 注销功能
- 错误处理和用户反馈

### [retrieve-user-from-db](retrieve-user-from-db)

从数据库中检索用户信息进行认证的示例项目。

数据库配置

```sql
-- DDL
CREATE TABLE IF NOT EXISTS t_user
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- DML
insert into t_user (id, username, email, password)
values (null, 'Tom', 'tom@mail.org', '{noop}123456'),
       (null, 'James', 'james@mail.org', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG');
```
