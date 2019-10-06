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

所配置的文件：

> https://github.com/liweinan/docker-hibernate/blob/master/hibernate-core/src/test/resources/hibernate.properties

使用配置的测试：

```bash
$ cd hibernate-core/src/test
$ ls
bundles   java      resources
$ grep -rl 'hibernate.properties' *
java/org/hibernate/test/collection/bag/BagDuplicatesTest.java
java/org/hibernate/test/legacy/Object2.hbm.xml
java/org/hibernate/test/legacy/MainObject.hbm.xml
java/org/hibernate/test/annotations/formula/FormulaNativeQueryTest.java
java/org/hibernate/test/tool/schema/IndividuallySchemaValidatorImplTest.java
java/org/hibernate/test/tool/schema/IndividuallySchemaValidatorImplConnectionTest.java
```

