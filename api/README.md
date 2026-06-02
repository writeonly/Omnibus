# API

This directory contains the JVM backend workspace for Omnibus.

## Layout

| Path | Purpose |
| --- | --- |
| [mono/config-server](mono/config-server/README.md) | Spring Cloud Config Server |
| [mono/eureka-server](mono) | Eureka service discovery |
| [mono/api-gateway](mono/api-gateway/README.md) | Spring Cloud Gateway entry point for backend traffic |
| [micro/auth-service](micro/auth-service/README.md) | Spring Authorization Server and auth flow support |
| [micro/user-service](micro/user-service/README.md) | User domain service |
| [micro/audit-service](micro/audit-service/README.md) | Kafka/audit observation service |
| [micro/rule-service](micro/rule-service/README.md) | Drools-based bridge bidding rule service |
| [micro/workflow-service](micro/workflow-service/README.md) | Rule-publication workflow service |
| `legacy/*` | Older or transitional services and projections |

## Backend Services

| Service | Responsibility | Local port |
| --- | --- | --- |
| `config-server` | Centralized Spring Cloud Config | `8888` |
| `eureka-server` | Service discovery for JVM services | `8761` |
| `api-gateway` | Spring Cloud Gateway entry point for backend services | `8080` |
| `auth-service` | Spring Authorization Server based authentication flows and JWT blacklist support | `8082` |
| `user-service` | User domain and registration flow | `8083` |
| `audit-service` | Audit/event observation service | `8085` |
| `rule-service` | Bridge hand parsing, Drools rule evaluation, managed rule storage | `8086` |
| `workflow-service` | Rule publication workflow and validation orchestration | `8087` |

## Build

```bash
./gradlew clean build
```

Run a single service during development:

```bash
./gradlew :rule-service:bootRun
./gradlew :workflow-service:bootRun
```

## Docker Compose

```bash
docker compose up --build
```

The compose file expects the shared `omnibus` Docker network and infrastructure services from `../infra` when external dependencies are needed.

## Communication Patterns

| Pattern | Used for |
| --- | --- |
| HTTP/REST | Frontend-facing APIs, development access, Swagger/OpenAPI surfaces |
| gRPC | Backend-to-backend calls through gateway/service clients |
| Kafka/Redpanda | Domain events, audit/archive projections, config bus use cases |
| Service discovery | JVM service lookup through Eureka |
| Centralized config | Shared service configuration through Spring Cloud Config |

## Module Notes

Keep service-specific commands, TODOs, API notes and design decisions in the README inside each service directory.

## TODO

- Use Spring Cloud Function
- Use Spring WebFlux when asynchronous request handling makes sense.
- Use Loom when synchronous request handling makes sense.
