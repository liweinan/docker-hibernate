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

`schemaupdate`的相关测试目录：

```bash
$ pwd
hibernate-core/src/test/java/org/hibernate/test/schemaupdate
```

目录里的测试文件：

```bash
$ ls
1_Version.hbm.xml                                          SchemaUpdateGeneratingOnlyScriptFileTest.java
2_Version.hbm.xml                                          SchemaUpdateHaltOnErrorTest.java
3_Version.hbm.xml                                          SchemaUpdateJoinColumnNoConstraintSecondaryTableTest.java
AbstractAlterTableQuoteSchemaTest.java                     SchemaUpdateJoinColumnNoConstraintSecondaryTablesTest.java
AlterTableQuoteDefaultSchemaTest.java                      SchemaUpdateJoinColumnNoConstraintTest.java
AlterTableQuoteSpecifiedSchemaTest.java                    SchemaUpdateProceedOnErrorTest.java
ColumnNamesTest.java                                       SchemaUpdateSQLServerTest.java
CommentGeneration.hbm.xml                                  SchemaUpdateSchemaNameTest.java
CommentGenerationTest.java                                 SchemaUpdateTableBackedSequenceTest.java
ConnectionsReleaseTest.java                                SchemaUpdateTest.java
ExportIdentifierTest.java                                  SchemaUpdateWithFunctionIndexTest.java
Group.java                                                 SchemaUpdateWithViewsTest.java
HANASchemaMigrationTargetScriptCreationTest.java           SequenceReadingTest.java
Hbm2ddlCreateOnlyTest.java                                 SqlServerQuoteSchemaTest.java
IdentifierHelperTest.java                                  TableCommentTest.java
ImplicitCompositeKeyJoinTest.java                          TestFkUpdating.java
MigrationTest.java                                         User.java
MixedFieldPropertyAnnotationTest.java                      UserGroup.hbm.xml
PostgreSQLMultipleSchemaSequenceTest.java                  Version.java
QuotedTableNameSchemaUpdateTest.java                       derivedid
QuotedTableNameWithForeignKeysSchemaUpdateTest.java        foreignkeys
SchemaDropTest.java                                        idbag
SchemaExportTest.java                                      idgenerator
SchemaExportWithGlobalQuotingEnabledTest.java              index
SchemaExportWithIndexAndDefaultSchema.java                 inheritance
SchemaMigrationTargetScriptCreationTest.java               manytomany
SchemaMigratorHaltOnErrorTest.java                         mapping.hbm.xml
SchemaUpdateDelimiterTest.java                             mapping2.hbm.xml
SchemaUpdateFormatterTest.java                             uniqueconstraint
```

测试用到的配置文件：

```bash
$ pwd
/Users/weli/works/docker-hibernate
```

```bash
$ grep -rl 'hibernate_orm_test' * | grep hibernate.properties
databases/mariadb/resources/hibernate.properties
databases/oracle/resources/hibernate.properties
databases/derby/resources/hibernate.properties
databases/pgsql/resources/hibernate.properties
databases/mssqlserver/resources/hibernate.properties
```

`pgsql`的配置：

* `databases/pgsql/resources/hibernate.properties`

内容：

```txt
#
# Hibernate, Relational Persistence for Idiomatic Java
#
# License: GNU Lesser General Public License (LGPL), version 2.1 or later.
# See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
#

hibernate.dialect org.hibernate.dialect.PostgreSQL94Dialect
hibernate.connection.driver_class org.postgresql.Driver
hibernate.connection.url jdbc:postgresql:hibernate_orm_test
hibernate.connection.username hibernate_orm_test
hibernate.connection.password hibernate_orm_test

hibernate.connection.pool_size 5

hibernate.show_sql false
hibernate.format_sql true

hibernate.max_fetch_depth 5

hibernate.cache.region_prefix hibernate.test
hibernate.cache.region.factory_class org.hibernate.testing.cache.CachingRegionFactory

hibernate.service.allow_crawling=false
hibernate.session.events.log=true
```
