# Authenticate

![minecraft_title](https://s2.loli.net/2025/07/27/sn4ucxUqjRKFSWa.png)

[English](README.md) | [中文](README_zh.md)

一个为服务器提供玩家身份验证功能的 Minecraft Fabric 模组。

[![Version](https://img.shields.io/github/v/tag/Enaium/fabric-mod-Authenticate?label=version&style=flat-square&logo=github)](https://github.com/Enaium/fabric-mod-Authenticate/releases)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1315021?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/authenticate)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/bsrcaiiY?style=flat-square&logo=modrinth)](https://modrinth.com/mod/authenticate)

## 描述

Authenticate 是一个服务器端身份验证模组，允许玩家注册带密码的账户并登录以访问服务器。此模组有助于保护您的服务器免受未经授权的访问，并提供基本的账户管理功能。

## 功能特性

- **玩家注册**：玩家可以使用密码注册账户
- **玩家登录**：安全的登录系统，带密码验证
- **玩家登出**：玩家可以登出账户
- **封禁系统**：服务器管理员可以封禁玩家
- **数据库存储**：使用 H2 数据库进行持久化数据存储
- **跨版本支持**：支持多个 Minecraft 版本

## 要求

- **Fabric Language Kotlin**：Kotlin 支持必需
- **Fabric ORM Jimmer**：数据库操作必需

### 可选依赖

- **Fabric Database H2**：H2 数据库支持

## 使用方法

### 命令

- `/register <密码> <确认>` - 注册新账户
- `/login <密码>` - 登录到您的账户
- `/logout` - 登出您的账户
- `/auth ban <玩家> <单位> <数量>` - 封禁玩家（仅管理员）

### 注册过程

1. 当新玩家加入服务器时，系统会提示他们注册
2. 使用 `/register <密码> <确认>` 创建账户
3. 确保两个密码匹配
4. 成功注册后，您可以使用 `/login <密码>` 登录

### 登录过程

1. 使用 `/login <密码>` 访问您的账户
2. 您必须登录才能与服务器交互
3. 游戏结束时使用 `/logout`

## 配置

模组在 `config` 文件夹中创建配置文件：

- `Authenticate.json` - 主配置文件

### 配置文件位置

配置文件会自动创建在：

```
./config/Authenticate.json
```

### 配置选项

`Authenticate.json` 文件包含以下配置选项：

#### `lang` (字符串)

- **默认值**：`"en_us"`
- **描述**：设置模组消息的语言
- **可用选项**：
    - `"en_us"` - 英语
    - `"zh_cn"` - 中文（简体）

#### `jdbcUrl` (字符串)

- **默认值**：`"jdbc:h2:file:./db/authenticate"`
- **描述**：数据库的 JDBC 连接 URL
- **说明**：
    - 默认使用 H2 数据库
    - 数据库文件存储在 `./db/authenticate.mv.db`
    - 您可以更改此设置以使用其他数据库（MySQL、PostgreSQL 等）

#### `username` (字符串，可选)

- **默认值**：`null`
- **描述**：数据库用户名（如果需要）
- **说明**：H2 数据库留空（不需要身份验证）

#### `password` (字符串，可选)

- **默认值**：`null`
- **描述**：数据库密码（如果需要）
- **说明**：H2 数据库留空（不需要身份验证）

#### `salt` (字符串)

- **默认值**：自动生成的 Base64 编码 UUID
- **描述**：用于密码哈希的盐值
- **说明**：
    - 首次运行时自动生成
    - 玩家注册后请勿更改此值
    - 更改盐值将使所有现有密码失效

#### `ddlStatement` (字符串)

- **默认值**：创建玩家表的 SQL DDL
- **描述**：创建数据库表的 SQL 语句
- **说明**：
    - 仅在您了解数据库架构设计时修改

### 配置示例

```json
{
  "lang": "zh_cn",
  "jdbcUrl": "jdbc:h2:file:./db/authenticate",
  "username": null,
  "password": null,
  "salt": "YXV0aGVudGljYXRlLXNhbHQ=",
  "ddlStatement": "create table if not exists player_v0 (id uuid primary key not null, created_time timestamp not null, modified_time timestamp not null, uuid uuid unique not null, password varchar(255) not null, banned timestamp);"
}
```

## 数据库

模组支持多种数据库系统来存储玩家身份验证数据。数据库连接通过配置文件中的 `jdbcUrl` 参数进行配置。

### 支持的数据库

#### H2 数据库（默认）

- **驱动**：内置 H2 驱动
- **默认 URL**：`jdbc:h2:file:./db/authenticate`
- **文件位置**：`./db/authenticate.mv.db`
- **优点**：
    - 无需额外设置
    - 基于文件，可移植
    - 适合中小型服务器
- **缺点**：
    - 并发连接有限
    - 不适合大规模部署

#### MySQL

- **驱动**：MySQL Connector/J
- **URL 格式**：`jdbc:mysql://主机名:端口/数据库名`
- **示例**：`jdbc:mysql://localhost:3306/authenticate`
- **优点**：
    - 高性能
    - 适合大型服务器
    - 稳定可靠
- **缺点**：
    - 需要单独的 MySQL 服务器设置
    - 需要额外依赖

#### PostgreSQL

- **驱动**：PostgreSQL JDBC 驱动
- **URL 格式**：`jdbc:postgresql://主机名:端口/数据库名`
- **示例**：`jdbc:postgresql://localhost:5432/authenticate`
- **优点**：
    - ACID 兼容
    - 优秀的数据完整性
    - 高级功能
- **缺点**：
    - 需要单独的 PostgreSQL 服务器
    - 设置更复杂

#### Oracle 数据库

- **驱动**：Oracle JDBC 驱动
- **URL 格式**：`jdbc:oracle:thin:@主机名:端口:服务名`
- **示例**：`jdbc:oracle:thin:@localhost:1521:XE`
- **优点**：
    - 企业级可靠性
    - 高级功能
    - 适合大型企业
- **缺点**：
    - 复杂的许可
    - 资源密集
    - 需要 Oracle 服务器设置

#### SQLite

- **驱动**：SQLite JDBC 驱动
- **URL 格式**：`jdbc:sqlite:文件路径`
- **示例**：`jdbc:sqlite:./db/authenticate.db`
- **优点**：
    - 轻量级且可移植
    - 无需服务器设置
    - 适合小型服务器
- **缺点**：
    - 并发访问有限
    - 不适合高流量服务器

### 数据库配置示例

#### H2 数据库

```json
{
  "jdbcUrl": "jdbc:h2:file:./db/authenticate",
  "username": null,
  "password": null
}
```

#### MySQL 数据库

```json
{
  "jdbcUrl": "jdbc:mysql://localhost:3306/authenticate?useSSL=false&serverTimezone=UTC",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### PostgreSQL 数据库

```json
{
  "jdbcUrl": "jdbc:postgresql://localhost:5432/authenticate",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### Oracle 数据库

```json
{
  "jdbcUrl": "jdbc:oracle:thin:@localhost:1521:XE",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### SQLite 数据库

```json
{
  "jdbcUrl": "jdbc:sqlite:./db/authenticate.db",
  "username": null,
  "password": null
}
```

### 数据库设置要求

#### MySQL 设置

1. 安装 MySQL 服务器
2. 创建数据库：`CREATE DATABASE authenticate;`
3. 创建用户：`CREATE USER 'authenticate_user'@'localhost' IDENTIFIED BY 'password';`
4. 授予权限：`GRANT ALL PRIVILEGES ON authenticate.* TO 'authenticate_user'@'localhost';`
5. 将 MySQL Connector/J 添加到模组依赖中

#### PostgreSQL 设置

1. 安装 PostgreSQL 服务器
2. 创建数据库：`CREATE DATABASE authenticate;`
3. 创建用户：`CREATE USER authenticate_user WITH PASSWORD 'password';`
4. 授予权限：`GRANT ALL PRIVILEGES ON DATABASE authenticate TO authenticate_user;`
5. 将 PostgreSQL JDBC 驱动添加到模组依赖中

#### Oracle 设置

1. 安装 Oracle 数据库
2. 创建表空间和用户
3. 授予必要权限
4. 将 Oracle JDBC 驱动添加到模组依赖中

#### SQLite 设置

1. 无需额外设置
2. 将 SQLite JDBC 驱动添加到模组依赖中

### 数据库迁移

在数据库系统之间切换时：

1. 备份现有数据
2. 更新配置中的 `jdbcUrl`、`username` 和 `password`
3. 重启服务器
4. 模组将自动创建所需的表结构

## 许可证

本项目采用 MIT 许可证 - 详情请参阅 [LICENSE](LICENSE) 文件。

## 问题反馈

如果您遇到任何问题，请在 [GitHub Issues 页面](https://github.com/Enaium/fabric-mod-Authenticate/issues) 上报告。