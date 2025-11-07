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
- 使用Spring Security进行安全保护
- 使用Thymeleaf渲染前端页面
- 使用H2内存数据库存储用户信息
- 密码加密存储
- 基本的表单验证
- 登录后重定向到欢迎页面
- 注册后自动登录
- 注销功能
- 错误处理和用户反馈
