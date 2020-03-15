* run on mssql server:

```bash
docker run -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=yourStrong(!)Password' -p 1433:1433 -d mcr.microsoft.com/mssql/server:2017-latest
```

```yaml
spring:
  application:
    name: demo
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql://localhost/demo
    username: jirah
    password: yourStrong(!)Password
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```


```kotlin
@Entity
data class Customer(var name: String? = null,
                    var email: String? = null,
                    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
                    var id: Long? = null)
```

```
-- the issue with identity
create table customer (id bigint identity not null, email varchar(255), name varchar(255), primary key (id))
```
