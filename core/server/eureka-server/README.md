# Eureka Server

Service discovery server for JVM backend services.

## Responsibility

`eureka-server` provides local service registration and discovery for the Spring services in `core`. Backend services register here and can discover each other without hardcoding every target address.

## Local Port

| Interface | Port |
| --- | --- |
| HTTP | `8761` |

## Development

```bash
cd ../..
./gradlew :eureka-server:bootRun
```

The service is also included in [../../docker-compose.yml](../../docker-compose.yml).
