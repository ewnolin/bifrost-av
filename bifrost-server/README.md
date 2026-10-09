# bifrost-server

Spring Boot verification service.

## Run locally
```
./gradlew :bifrost-server:bootRun --args="--spring.profiles.active=dev"
```

| URL | What |
|---|---|
| http://localhost:8080/actuator/health | Health check |
| http://localhost:8080/swagger-ui.html | API docs |
| http://localhost:8080/v3/api-docs | OpenAPI spec (JSON) |

## Test
```
./gradlew :bifrost-server:test
```
