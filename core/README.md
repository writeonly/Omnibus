# Services

This directory contains the JVM backend workspace for Omnibus.

## Layout

| Path | Purpose |
| --- | --- |
| `legacy/swing-client` | Desktop client prototype |
| `server/config-server` | Spring Cloud Config Server |
| `server/eureka-server` | Eureka service discovery |
| `server/auth-server` | Spring Authorization Server and auth flow support |
| `server/api-gateway` | Spring Cloud Gateway entry point for backend traffic |
| `service/audit-service` | Kafka/audit observation service |
| `service/user-service` | User domain service |
| `service/rule-engine` | Drools-based bridge bidding rule engine |
| `service/workflow-engine` | Rule-publication workflow service |
| `legacy/*` | Older/transitional services and projections |

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

| Service | Port |
| --- | --- |
| config-server | `8888` |
| eureka-server | `8761` |
| auth-server | `9000` |
| api-gateway | `8080` |
| audit-service | `8081` |
| user-service | `8082` |
| rule-engine | `8083` |
| workflow-engine | `8084` |

## Module Notes

Keep service-specific commands, TODOs, API notes, and design decisions in the README inside each service directory.

## TODO

* Use String Webflux when asynchronous makes sense
* Use Spring Cloud Function 
