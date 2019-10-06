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

