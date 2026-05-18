# Omnibus

Omnibus is a bridge bidding platform organized as a monorepo. The current architecture combines JVM microservices, a NestJS backend-for-frontend, frontend applications, and local infrastructure for identity, messaging, persistence, and observability.

The central project documentation lives here in the repository root. Module-specific notes stay next to the module they describe.

## Project Map

| Area | Path | Purpose |
| --- | --- | --- |
| Backend services | [services/README.md](services/README.md) | Spring Boot/Kotlin services, gRPC/HTTP APIs, rule processing, workflow, auth, audit, config and discovery |
| Presentation | [presentation/README.md](presentation/README.md) | Angular/React clients and NestJS BFF |
| Infrastructure | [infra/README.md](infra/README.md) | Local infrastructure: PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak, Redis, RabbitMQ, search stack, NGINX |
| Observability | [obs/docker-compose.yml](obs/docker-compose.yml) | Prometheus, Loki, Grafana stack |
| Architecture | [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) | Technical architecture and request flows |
| Documentation index | [docs/README.md](docs/README.md) | Pointers to project and module documentation |

## Architecture At A Glance

```text
Frontend apps
  Angular primary UI, React experiments, developer dashboard
        |
        v
NestJS BFF
  HTTP facade, frontend integration, auth/session boundary
        |
        v
API Gateway
  Spring Cloud Gateway, security filters, routing, gRPC bridge
        |
        v
Core services
  rule-engine, workflow-engine, user-service, audit-service,
  auth-server, config-server, eureka-server
        |
        v
Infrastructure
  PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak,
  Redis, RabbitMQ, Elasticsearch/OpenSearch, MongoDB/FerretDB
```

The most important product path is the bidding recommendation flow:

```text
User enters a bridge hand
  -> frontend sends it to the BFF
  -> BFF routes through the API gateway
  -> rule-engine parses the hand and evaluates Drools rules
  -> recommendation is returned synchronously
  -> domain events can be published to Kafka and archived/audited
```

Admin and rule-publication flows are handled by `workflow-engine`, which validates rule submissions against `rule-engine` before publication.

## Main Components

| Component | Path | Notes |
| --- | --- | --- |
| Angular frontend | [presentation/frontend-angular/README.md](presentation/frontend-angular/README.md) | Primary frontend |
| React frontend | [presentation/frontend-react/README.md](presentation/frontend-react/README.md) | Alternative frontend |
| Developer dashboard | [presentation/dev-dashboard/README.md](presentation/dev-dashboard/README.md) | Vite/React dashboard |
| NestJS BFF | [presentation/bff-nest/README.md](presentation/bff-nest/README.md) | HTTP-to-gRPC/backend facade |
| API gateway | [services/core/api-gateway/README.md](services/core/api-gateway/README.md) | Spring Cloud Gateway |
| Rule engine | [services/core/rule-engine/README.md](services/core/rule-engine/README.md) | Drools-based bridge bidding rules |
| Workflow engine | [services/core/workflow-engine/README.md](services/core/workflow-engine/README.md) | Rule-publication workflow |
| Auth server | [services/core/auth-server/README.md](services/core/auth-server/README.md) | Spring Authorization Server wrapper around Keycloak-oriented flows |
| User service | [services/core/user-service/README.md](services/core/user-service/README.md) | User registration domain |
| Audit service | [services/core/audit-service/README.md](services/core/audit-service/README.md) | Kafka traffic/audit persistence |
| Config server | [services/core/config-server/README.md](services/core/config-server/README.md) | Spring Cloud Config |
| Swing client | [services/core/swing-client/README.md](services/core/swing-client/README.md) | Desktop client prototype |

## Local Startup

The repository is split into separate compose files for infrastructure, backend services, presentation apps, and observability.

### 1. Infrastructure

```bash
cd infra
docker compose up --build
```

Useful local ports:

| Service | URL/Port |
| --- | --- |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9090` |
| Redis | `localhost:6379` |
| RabbitMQ | `http://localhost:15672` |
| Kafka external listener | `localhost:29092` when the `first` profile is enabled |
| Kafka UI | `http://localhost:9002` when the `first` profile is enabled |
| NGINX legacy gateway | `http://localhost:8000` when the `legacy` profile is enabled |

Profiles in [infra/docker-compose.yml](infra/docker-compose.yml):

- `first`: Kafka, Cassandra, Elasticsearch, Kibana, MongoDB
- `follow`: Redpanda, Scylla, OpenSearch, OpenSearch Dashboards, FerretDB
- `legacy`: NGINX gateway

### 2. Backend Services

```bash
cd services
docker compose up --build
```

Primary service ports:

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

For local JVM development:

```bash
cd services
./gradlew clean build
./gradlew :rule-engine:bootRun
```

### 3. Presentation Apps

```bash
cd presentation
docker compose up --build
```

Primary presentation ports:

| App | URL |
| --- | --- |
| Angular frontend container | `http://localhost:4300` |
| NestJS BFF | `http://localhost:3000` |

For local frontend development, use the README inside the selected module.

## Development Notes

- Java services use JDK 21 and Gradle Kotlin DSL.
- Core services are listed in [services/settings.gradle.kts](services/settings.gradle.kts).
- Frontend and BFF modules use Node/npm.
- Drools rules are in [services/core/rule-engine/src/main/resources/rules](services/core/rule-engine/src/main/resources/rules).
- Managed rules are stored under [services/core/rule-engine/managed-rules](services/core/rule-engine/managed-rules).
- BPMN workflow assets live in [services/core/workflow-engine/src/main/resources/bpmn](services/core/workflow-engine/src/main/resources/bpmn).

## Documentation Policy

- Keep this root README as the first stop for onboarding and current run instructions.
- Keep architecture details in [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md).
- Keep module-specific commands, TODOs, and design notes inside that module's `README.md`.
- Use [docs/README.md](docs/README.md) as a compact documentation index, not as a second full project README.

## License

MIT. See [docs/LICENSE](docs/LICENSE).
