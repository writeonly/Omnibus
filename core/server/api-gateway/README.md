# API Gateway

Spring Cloud Gateway entry point for backend traffic.

## Responsibility

`api-gateway` is the backend boundary used by the BFF and clients during local development. It handles routing, security filters and gRPC bridge configuration for core services.

## Local Port

| Interface | Port |
| --- | --- |
| HTTP | `8080` |

## Backend Boundary

```text
NestJS BFF
  -> API Gateway
  -> rule-engine / workflow-engine / user-service / audit-service
```

The gateway is configured through `config-server` and discovers JVM services through `eureka-server`.

## Development

```bash
cd ../..
./gradlew :api-gateway:bootRun
```

The service is also included in [../../docker-compose.yml](../../docker-compose.yml).

## TODO

- Convert gRPC integration to streaming where it is useful.
- Use Spring Security and Resource Server support consistently.
- Use Spring Cloud LoadBalancer for HTTP service calls.
- Use Spring Cloud Stream/Kafka where an event boundary is better than direct calls.
