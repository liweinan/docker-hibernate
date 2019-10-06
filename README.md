启动数据库：

```bash
$ pg_ctl -D /usr/local/var/postgres start
```

连接数据库：

```bash
$ psql postgres
psql (11.4)
Type "help" for help.

postgres=#
```

创建数据库和用户：

```bash
postgres=# create user foo_usr with password 'foo';
CREATE ROLE
postgres=# create database foo_db with owner foo_usr;
CREATE DATABASE
postgres=#
```

数据库配置：

> https://github.com/liweinan/docker-hibernate/blob/master/config.patch

