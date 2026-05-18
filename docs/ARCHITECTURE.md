# Omnibus Architecture

Omnibus is a bridge bidding platform built as a monorepo. It is organized around a presentation layer, a backend service layer, and local infrastructure that supports authentication, discovery, messaging, persistence, and observability.

## System Context

```text
Users / Admins
    |
    v
Presentation layer
    Angular frontend, React frontend, developer dashboard
    |
    v
NestJS BFF
    HTTP API for frontend integration
    |
    v
API Gateway
    Spring Cloud Gateway, route/security boundary, gRPC bridge
    |
    v
Core services
    rule-engine, workflow-engine, user-service, auth-server,
    audit-service, config-server, eureka-server
    |
    v
Infrastructure
    PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak,
    Redis, RabbitMQ, Elasticsearch/OpenSearch, MongoDB/FerretDB
```

## Repository Layout

```text
Omnibus/
├── README.md
├── docs/
│   ├── README.md
│   ├── ARCHITECTURE.md
│   └── LICENSE
├── services/
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   ├── docker-compose.yml
│   ├── core/
│   │   ├── api-gateway/
│   │   ├── audit-service/
│   │   ├── auth-server/
│   │   ├── config-server/
│   │   ├── eureka-server/
│   │   ├── rule-engine/
│   │   ├── swing-client/
│   │   ├── user-service/
│   │   └── workflow-engine/
│   └── legacy/
│       ├── event-archive/
│       ├── outbox-relay-service/
│       └── cassandra-projection-service/
├── presentation/
│   ├── docker-compose.yml
│   ├── bff-nest/
│   ├── dev-dashboard/
│   ├── frontend-angular/
│   └── frontend-react/
├── infra/
│   ├── docker-compose.yml
│   ├── cassandra/
│   ├── keycloak/
│   ├── nginx/
│   ├── outbox-relay/
│   └── postgres/
└── obs/
    └── docker-compose.yml
```

## Backend Services

| Service | Responsibility | Primary local port |
| --- | --- | --- |
| `config-server` | Centralized Spring Cloud Config | `8888` |
| `eureka-server` | Service discovery for JVM services | `8761` |
| `auth-server` | Spring Authorization Server based authentication flows and JWT blacklist support | `9000` |
| `api-gateway` | Spring Cloud Gateway entry point for backend services | `8080` |
| `audit-service` | Audit/event observation service | `8081` |
| `user-service` | User domain and registration flow | `8082` |
| `rule-engine` | Bridge hand parsing, Drools rule evaluation, managed rule storage | `8083` |
| `workflow-engine` | Rule publication workflow and validation orchestration | `8084` |

The backend build is rooted in [services/settings.gradle.kts](../services/settings.gradle.kts). Most active JVM modules live under `services/core`; older or transitional services live under `services/legacy`.

## Presentation Layer

| Module | Responsibility |
| --- | --- |
| `presentation/frontend-angular` | Primary frontend |
| `presentation/frontend-react` | Alternative frontend implementation |
| `presentation/dev-dashboard` | Developer dashboard |
| `presentation/bff-nest` | Backend-for-frontend that shields UI clients from backend topology |

The presentation compose file exposes the Angular container on `4300` and the BFF on `3000`.

## Infrastructure Layer

[infra/docker-compose.yml](../infra/docker-compose.yml) defines shared local services. It uses profiles so the stack can be adjusted to the current development path.

| Profile | Adds |
| --- | --- |
| `first` | Kafka, Kafka UI, Cassandra, Elasticsearch, Kibana, MongoDB |
| `follow` | Redpanda, Scylla, OpenSearch, OpenSearch Dashboards, FerretDB |
| `legacy` | NGINX gateway on `8000` |

Always-on infrastructure includes PostgreSQL, Redis, RabbitMQ, and Keycloak.

## Recommendation Flow

```text
1. User enters a bridge hand in a frontend.
2. Frontend sends a request to the NestJS BFF.
3. BFF calls the backend through the API gateway boundary.
4. API gateway routes to the rule-engine.
5. rule-engine parses the hand into facts.
6. Drools evaluates bundled and managed rules.
7. rule-engine returns a recommendation synchronously.
8. rule-engine may publish domain events for audit/archive projections.
```

The rule-engine contains:

- REST API controllers for direct development access.
- gRPC service classes for backend-to-backend integration.
- Drools compilation/evaluation services.
- Managed rule administration and storage.
- Kafka event publication abstractions.

## Rule Publication Flow

```text
1. Admin submits a rule publication request.
2. Request enters the backend through the BFF/gateway boundary.
3. workflow-engine starts the publication workflow.
4. workflow-engine calls rule-engine to validate the full ruleset.
5. rule-engine compiles the candidate Drools rules.
6. Valid rules can be persisted as managed rules.
7. A rule update event can be published for downstream consumers.
```

Workflow assets live in [services/core/workflow-engine/src/main/resources/bpmn](../services/core/workflow-engine/src/main/resources/bpmn). Managed rule files live in [services/core/rule-engine/managed-rules](../services/core/rule-engine/managed-rules).

## Communication Patterns

| Pattern | Used for |
| --- | --- |
| HTTP/REST | Frontend-facing APIs, development access, Swagger/OpenAPI surfaces |
| gRPC | Backend-to-backend calls through gateway/service clients |
| Kafka/Redpanda | Domain events, audit/archive projections, config bus use cases |
| Service discovery | JVM service lookup through Eureka |
| Centralized config | Shared service configuration through Spring Cloud Config |

## Data And State

| Store | Current role |
| --- | --- |
| PostgreSQL | Service databases and auth-related state |
| Redis | Cache and token blacklist support |
| Cassandra/Scylla | Event/audit/projection storage path |
| MongoDB/FerretDB | Audit/search-oriented storage experiments |
| Elasticsearch/OpenSearch | Search and log/audit exploration |
| File storage | Managed Drools rules in the rule-engine module |

## Operational Notes

- Build backend services from `services` with Gradle.
- Run local infrastructure from `infra`.
- Run presentation containers from `presentation`.
- Use module README files for focused commands and TODOs.
- Keep this document for cross-module architecture decisions and flows.
