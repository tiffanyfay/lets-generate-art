# Things for ClickHouse

## Useful resources
* [Working With ClickHouse From Spring Data Using MySql Driver](https://jaitl.pro/post/2021/04/16/clickhouse-spring-data/)  
  * [Kotlin Code](https://github.com/jaitl/clickhouse-spring-data-demo/tree/main)
* [ClickHouse Java JDBC examples](https://github.com/ClickHouse/clickhouse-java/tree/main/examples/jdbc)
* [ClickHouse Java integration docs](https://clickhouse.com/docs/en/integrations/java)
* [Integrating Spring Boot with ClickHouse Database](https://towardsdev.com/integrating-spring-boot-with-clickhouse-database-dce77bebe50f)

## Errors and Solutions

### Quotes around Host URL
```xml

```
Fix: just remove quotes

### Classifier needed
Error:
```

```

Fix:
```xml
		<dependency>
			<groupId>com.clickhouse</groupId>
			<artifactId>clickhouse-jdbc</artifactId>
			<version>0.6.5</version>
			<classifier>http</classifier>
		</dependency>
```

### Dialect
```
Caused by: org.springframework.data.jdbc.repository.config.DialectResolver$NoDialectException: 
Cannot determine a dialect for org.springframework.jdbc.core.JdbcTemplate@273fa9e; Please provide a Dialect
```

Fix:
Since there isn't a dialect (that I could find by default -- there is 
[mention of it](https://github.com/ClickHouse/clickhouse-java/blob/0a7b9dea004dc48b34e9b7a4347b8f507a7f2e90/clickhouse-jdbc/src/main/java/com/clickhouse/jdbc/JdbcConfig.java#L310) in the Java client), we need to use
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
```
Not `spring-boot-starter-data-jdbc`