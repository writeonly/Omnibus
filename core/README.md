# Core

This directory contains the JVM backend workspace for Omnibus.

## Layout

| Path | Purpose |
| --- | --- |
| [server/config-server](server/config-server/README.md) | Spring Cloud Config Server |
| [server/eureka-server](server/eureka-server/README.md) | Eureka service discovery |
| [server/auth-server](server/auth-server/README.md) | Spring Authorization Server and auth flow support |
| [server/api-gateway](server/api-gateway/README.md) | Spring Cloud Gateway entry point for backend traffic |
| [service/audit-service](service/audit-service/README.md) | Kafka/audit observation service |
| [service/user-service](service/user-service/README.md) | User domain service |
| [service/rule-engine](service/rule-engine/README.md) | Drools-based bridge bidding rule engine |
| [service/workflow-engine](service/workflow-engine/README.md) | Rule-publication workflow service |
| `legacy/*` | Older or transitional services and projections |

## Backend Services

| Service | Responsibility | Local port |
| --- | --- | --- |
| `config-server` | Centralized Spring Cloud Config | `8888` |
| `eureka-server` | Service discovery for JVM services | `8761` |
| `auth-server` | Spring Authorization Server based authentication flows and JWT blacklist support | `9000` |
| `api-gateway` | Spring Cloud Gateway entry point for backend services | `8080` |
| `audit-service` | Audit/event observation service | `8081` |
| `user-service` | User domain and registration flow | `8082` |
| `rule-engine` | Bridge hand parsing, Drools rule evaluation, managed rule storage | `8083` |
| `workflow-engine` | Rule publication workflow and validation orchestration | `8084` |

## Build

```bash
./gradlew clean build
```

Run a single service during development:

```bash
./gradlew :rule-engine:bootRun
./gradlew :workflow-engine:bootRun
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

- Use Spring WebFlux when asynchronous request handling makes sense.
- Use Spring Cloud Function where it simplifies event/function boundaries.
