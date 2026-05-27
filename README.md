# Omnibus

Omnibus is a bridge bidding platform organized as a monorepo. The project combines JVM backend services, a NestJS backend-for-frontend, frontend applications, and local infrastructure for identity, messaging, persistence, and observability.

Central, cross-project information lives in this root README. Details that belong to one part of the system live in the README next to that code.

## Project Map

| Area | Path | Purpose |
| --- | --- | --- |
| Backend | [core/README.md](core/README.md) | Spring Boot/Kotlin services, HTTP/gRPC APIs, rule processing, workflow, auth, audit, config and discovery |
| Frontend and BFF | [ui/README.md](ui/README.md) | Angular and React clients, developer dashboard, NestJS BFF |
| Infrastructure | [infra/README.md](infra/README.md) | PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak, Redis, RabbitMQ, search stack, NGINX |
| Observability | [obs/README.md](obs/README.md) | Prometheus, Loki, Promtail and Grafana |
| Protobuf contracts | [proto/omnibus/v1](proto/omnibus/v1) | Shared gRPC/API contract definitions |

## Architecture At A Glance

```text
Users / Admins
    |
    v
Frontend apps
  Angular primary UI, React frontend, developer dashboard
    |
    v
NestJS BFF
  HTTP facade for frontend integration
    |
    v
API Gateway
  Spring Cloud Gateway, security filters, routing, gRPC bridge
    |
    v
Core services
  rule-service, workflow-service, user-service, audit-service,
  auth-server, config-server, eureka-server
    |
    v
Infrastructure
  PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak,
  Redis, RabbitMQ, Elasticsearch/OpenSearch, MongoDB/FerretDB
```

The main product path is the bidding recommendation flow:

```text
User enters a bridge hand
  -> frontend sends it to the BFF
  -> BFF routes through the API gateway
  -> rule-service parses the hand and evaluates Drools rules
  -> recommendation is returned synchronously
  -> domain events can be published to Kafka and archived/audited
```

Admin and rule-publication flows are handled by `workflow-service`, which validates rule submissions against `rule-service` before publication.

## Local Startup

Start the shared infrastructure first:

```bash
cd infra
docker compose up --build
```

Then start backend services:

```bash
cd core
docker compose up --build
```

Then start UI/BFF services:

```bash
cd ui
docker compose up --build
```

Optional observability stack:

```bash
cd obs
docker compose up --build
```

## Main Local Ports

| Component | URL/Port |
| --- | --- |
| Angular frontend container | `http://localhost:4200` |
| NestJS BFF | `http://localhost:3001` |
| API gateway | `http://localhost:8080` |
| config-server | `http://localhost:8888` |
| eureka-server | `http://localhost:8761` |
| auth-server | `http://localhost:9000` |
| rule-service | `http://localhost:8084` |
| workflow-service | `http://localhost:8085` |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9001` |
| RabbitMQ Management | `http://localhost:15672` |
| Grafana | `http://localhost:3001` |

See the README in each area for more ports, profiles and local development commands.

## Repository Helpers

| Script | Purpose |
| --- | --- |
| [list-files.sh](list-files.sh) | Repository file listing helper |
| [core/clean-gradle.sh](core/clean-gradle.sh) | Gradle cleanup helper for the backend workspace |
| [ui/bff-nest/run.sh](ui/bff-nest/run.sh) | Run the NestJS BFF |
| [ui/frontend-angular/run.sh](ui/frontend-angular/run.sh) | Run the Angular frontend |
| [ui/frontend-angular/openapi.sh](ui/frontend-angular/openapi.sh) | Angular OpenAPI generation helper |
| [ui/frontend-angular/orval.sh](ui/frontend-angular/orval.sh) | Orval generation helper |
| [ui/frontend-react/run.sh](ui/frontend-react/run.sh) | Run the React frontend |

## Development Notes

- Java services use JDK 21 and Gradle Kotlin DSL.
- Backend modules are listed in [core/settings.gradle.kts](core/settings.gradle.kts).
- Frontend and BFF modules use Node/npm.
- Drools rules are in [core/service/rule-service/src/main/resources/rules](core/service/rule-service/src/main/resources/rules).
- BPMN workflow assets live in [core/service/workflow-service/src/main/resources/bpmn](core/service/workflow-service/src/main/resources/bpmn).
- Shared protobuf definitions live in [proto/omnibus/v1](proto/omnibus/v1), with service-local copies where needed by build tooling.

## Documentation Policy

- Keep this root README as the first stop for onboarding, repository map and cross-module flows.
- Keep module-specific commands, TODOs and design notes inside that module's `README.md`.
- Keep service-specific behavior next to the service implementation.
- There is intentionally no `docs/` directory; avoid reintroducing one unless a new documentation format needs it.

## License

MIT. See [LICENSE](LICENSE).
