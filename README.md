# Authenticate

[English](README.md) | [中文](README_zh.md)

A Minecraft Fabric mod that provides player authentication functionality for servers.

[![Version](https://img.shields.io/github/v/tag/Enaium/fabric-mod-Authenticate?label=version&style=flat-square&logo=github)](https://github.com/Enaium/fabric-mod-Authenticate/releases)
[![CurseForge Downloads](https://img.shields.io/curseforge/dt/1315021?style=flat-square&logo=curseforge)](https://www.curseforge.com/minecraft/mc-mods/authenticate)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/bsrcaiiY?style=flat-square&logo=modrinth)](https://modrinth.com/mod/authenticate)

## Description

Authenticate is a server-side authentication mod for Minecraft that allows players to register accounts with passwords
and login to access the server. This mod helps protect your server from unauthorized access and provides basic account
management features.

## Features

- **Player Registration**: Players can register accounts with passwords
- **Player Login**: Secure login system with password verification
- **Player Logout**: Players can logout from their accounts
- **Ban System**: Server administrators can ban players
- **Database Storage**: Uses H2 database for persistent data storage
- **Cross-version Support**: Supports multiple Minecraft versions

## Requirements

- **Fabric Language Kotlin**: Required for Kotlin support
- **Fabric ORM Jimmer**: Required for database operations

### Optional Dependencies

- **Fabric Database H2**: For H2 database support

## Usage

### Commands

- `/register <password> <confirm>` - Register a new account
- `/login <password>` - Login to your account
- `/logout` - Logout from your account
- `/ban <player> <unit> <amount>` - Ban a player (admin only)

### Registration Process

1. When a new player joins the server, they will be prompted to register
2. Use `/register <password> <confirm>` to create an account
3. Make sure both passwords match
4. After successful registration, you can login with `/login <password>`

### Login Process

1. Use `/login <password>` to access your account
2. You must be logged in to interact with the server
3. Use `/logout` when you're done playing

## Configuration

The mod creates configuration files in the `config` folder:

- `Authenticate.json` - Main configuration file

### Configuration File Location

The configuration file is automatically created at:

```
./config/Authenticate.json
```

### Configuration Options

The `Authenticate.json` file contains the following configuration options:

#### `lang` (String)

- **Default**: `"en_us"`
- **Description**: Sets the language for the mod's messages
- **Available Options**:
    - `"en_us"` - English
    - `"zh_cn"` - Chinese (Simplified)

#### `jdbcUrl` (String)

- **Default**: `"jdbc:h2:file:./db/authenticate"`
- **Description**: The JDBC connection URL for the database
- **Notes**:
    - Uses H2 database by default
    - Database file is stored in `./db/authenticate.mv.db`
    - You can change this to use other databases (MySQL, PostgreSQL, etc.)

#### `username` (String, Optional)

- **Default**: `null`
- **Description**: Database username (if required)
- **Notes**: Leave as `null` for H2 database (no authentication required)

#### `password` (String, Optional)

- **Default**: `null`
- **Description**: Database password (if required)
- **Notes**: Leave as `null` for H2 database (no authentication required)

#### `salt` (String)

- **Default**: Auto-generated Base64 encoded UUID
- **Description**: Salt used for password hashing
- **Notes**:
    - Automatically generated on first run
    - Do not change this value after players have registered
    - Changing the salt will invalidate all existing passwords

#### `ddlStatement` (String)

- **Default**: SQL DDL for creating the player table
- **Description**: SQL statement to create the database table
- **Notes**:
    - Only modify if you understand database schema design

### Example Configuration

```json
{
  "lang": "en_us",
  "jdbcUrl": "jdbc:h2:file:./db/authenticate",
  "username": null,
  "password": null,
  "salt": "YXV0aGVudGljYXRlLXNhbHQ=",
  "ddlStatement": "create table if not exists player_v0 (id uuid primary key not null, created_time timestamp not null, modified_time timestamp not null, uuid uuid unique not null, password varchar(255) not null, banned timestamp);"
}
```

## Database

The mod supports multiple database systems for storing player authentication data. The database connection is configured
through the `jdbcUrl` parameter in the configuration file.

### Supported Databases

#### H2 Database (Default)

- **Driver**: Built-in H2 driver
- **Default URL**: `jdbc:h2:file:./db/authenticate`
- **File Location**: `./db/authenticate.mv.db`
- **Pros**:
    - No additional setup required
    - File-based, portable
    - Good for small to medium servers
- **Cons**:
    - Limited concurrent connections
    - Not suitable for large-scale deployments

#### MySQL

- **Driver**: MySQL Connector/J
- **URL Format**: `jdbc:mysql://hostname:port/database_name`
- **Example**: `jdbc:mysql://localhost:3306/authenticate`
- **Pros**:
    - High performance
    - Excellent for large servers
    - Robust and reliable
- **Cons**:
    - Requires separate MySQL server setup
    - Additional dependency needed

#### PostgreSQL

- **Driver**: PostgreSQL JDBC Driver
- **URL Format**: `jdbc:postgresql://hostname:port/database_name`
- **Example**: `jdbc:postgresql://localhost:5432/authenticate`
- **Pros**:
    - ACID compliant
    - Excellent data integrity
    - Advanced features
- **Cons**:
    - Requires separate PostgreSQL server
    - More complex setup

#### Oracle Database

- **Driver**: Oracle JDBC Driver
- **URL Format**: `jdbc:oracle:thin:@hostname:port:service_name`
- **Example**: `jdbc:oracle:thin:@localhost:1521:XE`
- **Pros**:
    - Enterprise-grade reliability
    - Advanced features
    - Excellent for large enterprises
- **Cons**:
    - Complex licensing
    - Resource intensive
    - Requires Oracle server setup

#### SQLite

- **Driver**: SQLite JDBC Driver
- **URL Format**: `jdbc:sqlite:file_path`
- **Example**: `jdbc:sqlite:./db/authenticate.db`
- **Pros**:
    - Lightweight and portable
    - No server setup required
    - Good for small servers
- **Cons**:
    - Limited concurrent access
    - Not suitable for high-traffic servers

### Database Configuration Examples

#### H2 Database

```json
{
  "jdbcUrl": "jdbc:h2:file:./db/authenticate",
  "username": null,
  "password": null
}
```

#### MySQL Database

```json
{
  "jdbcUrl": "jdbc:mysql://localhost:3306/authenticate?useSSL=false&serverTimezone=UTC",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### PostgreSQL Database

```json
{
  "jdbcUrl": "jdbc:postgresql://localhost:5432/authenticate",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### Oracle Database

```json
{
  "jdbcUrl": "jdbc:oracle:thin:@localhost:1521:XE",
  "username": "authenticate_user",
  "password": "your_password"
}
```

#### SQLite Database

```json
{
  "jdbcUrl": "jdbc:sqlite:./db/authenticate.db",
  "username": null,
  "password": null
}
```

### Database Setup Requirements

#### For MySQL

1. Install MySQL Server
2. Create database: `CREATE DATABASE authenticate;`
3. Create user: `CREATE USER 'authenticate_user'@'localhost' IDENTIFIED BY 'password';`
4. Grant permissions: `GRANT ALL PRIVILEGES ON authenticate.* TO 'authenticate_user'@'localhost';`
5. Add MySQL Connector/J to your mod dependencies

#### For PostgreSQL

1. Install PostgreSQL Server
2. Create database: `CREATE DATABASE authenticate;`
3. Create user: `CREATE USER authenticate_user WITH PASSWORD 'password';`
4. Grant permissions: `GRANT ALL PRIVILEGES ON DATABASE authenticate TO authenticate_user;`
5. Add PostgreSQL JDBC Driver to your mod dependencies

#### For Oracle

1. Install Oracle Database
2. Create tablespace and user
3. Grant necessary permissions
4. Add Oracle JDBC Driver to your mod dependencies

#### For SQLite

1. No additional setup required
2. Add SQLite JDBC Driver to your mod dependencies

### Database Migration

When switching between database systems:

1. Backup your existing data
2. Update the `jdbcUrl`, `username`, and `password` in configuration
3. Restart the server
4. The mod will automatically create the required table structure

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Issues

If you encounter any issues, please report them on
the [GitHub Issues page](https://github.com/Enaium/fabric-mod-Authenticate/issues).