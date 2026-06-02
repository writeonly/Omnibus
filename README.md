# Omnibus

Omnibus is a bridge bidding platform organized as a monorepo. The project combines JVM backend services, a NestJS backend-for-frontend, frontend applications, and local infrastructure for identity, messaging, persistence, and observability.

Central, cross-project information lives in this root README. Details that belong to one part of the system live in the README next to that code.

## Project Map

| Area | Path | Purpose |
| --- | --- | --- |
| Backend | [core/README.md](core/README.md) | Spring Boot/Kotlin services, HTTP/gRPC APIs, rule processing, workflow, auth, audit, config and discovery |
| Frontend and BFF | [ui/README.md](ui/README.md) | Angular primary frontend, experimental React frontends, NestJS BFF |
| Infrastructure | [infra/README.md](infra/README.md) | PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak, Redis, RabbitMQ, search stack, NGINX, Prometheus, Loki, Grafana |
| Protobuf contracts | [proto/omnibus/v1](proto/omnibus/v1) | Shared gRPC/API contract definitions |

## Architecture At A Glance

```text
Users / Admins
    |
    v
Frontend apps
  Angular primary UI, experimental React frontends (Vite, Next.js), developer dashboard
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
  auth-service, config-server, eureka-server
    |
    v
Infrastructure
  PostgreSQL, Kafka/Redpanda, Cassandra/Scylla, Keycloak,
  Redis, RabbitMQ, Elasticsearch/OpenSearch, MongoDB/FerretDB
```

## Main Flows

### Bidding Recommendation Flow

The main product path for generating bridge bidding recommendations:

```text
User enters a bridge hand
  -> frontend sends it to the BFF
  -> BFF routes through the API gateway
  -> rule-service parses the hand and evaluates Drools rules
  -> recommendation is returned synchronously
  -> domain events can be published to Kafka and archived/audited
```

### User Registration Flow

Complete user registration flow with asynchronous processing:

```text
Client
  |
  v (HTTP)
frontend (Angular/React)
  |
  v (HTTP)
bff-nest
  |
  v (HTTP)
api-gateway (Spring Cloud Gateway)
  |
  v (gRPC)
user-service
  |
  v
PostgreSQL
  |
  v (Outbox pattern)
Outbox table
  |
  v (async publish)
Message Bus (RabbitMQ or Kafka)
  |
  v (subscribe)
auth-service
  |
  v (Spring Cloud Function)
Keycloak
  |
  v (create user)
Identity Provider
```

**Flow Details:**
1. Client submits registration form through frontend (Angular or React)
2. Frontend sends HTTP request to NestJS BFF
3. BFF translates and forwards to Spring Cloud API Gateway
4. API Gateway routes to user-service via gRPC
5. user-service validates and stores user in PostgreSQL
6. Domain event written to Outbox table (transactional)
7. Outbox relay publishes event to RabbitMQ or Kafka
8. auth-service subscribes to registration event
9. Spring Cloud Function triggers Keycloak integration
10. Keycloak creates identity account
11. Response propagates back through the chain

**Key Patterns:**
- **Outbox Pattern**: Ensures consistency between user creation and event publishing
- **Event-Driven**: Decoupled auth-service from user-service via message bus
- **Request/Response**: Synchronous path through API layers for immediate feedback
- **Transactional Boundary**: PostgreSQL transaction includes both user and outbox record

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

## Main Local Ports

| Component | URL/Port |
| --- | --- |
| **Angular frontend (primary)** | `http://localhost:4200` |
| NestJS BFF | `http://localhost:3001` |
| API gateway | `http://localhost:8080` |
| **Experimental frontends** | |
| React Vite frontend | `http://localhost:5173` |
| React Next.js frontend | `http://localhost:3002` |
| Developer dashboard | `http://localhost:5174` |
| **Backend services** | |
| config-server | `http://localhost:8888` |
| eureka-server | `http://localhost:8761` |
| auth-service | `http://localhost:8083` |
| rule-service | `http://localhost:8085` |
| workflow-service | `http://localhost:8086` |
| **Infrastructure** | |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9000` |
| Redis | `localhost:6379` |
| RabbitMQ | `localhost:5672` |
| RabbitMQ Management | `http://localhost:15672` |
| **Observability** | |
| Prometheus | `http://localhost:9090` |
| Loki | `http://localhost:3100` |
| Grafana | `http://localhost:3000` |

See the README in each area for more ports, profiles and local development commands.

## Frontend Applications

The `ui/` directory contains multiple frontend implementations:

| Application | Technology | Port | Status | Purpose |
| --- | --- | --- | --- | --- |
| frontend-angular | Angular | `4200` | ✅ Primary | Main production frontend |
| frontend-react-vite | React + TypeScript + Vite | `5173` | 🔬 Experimental | Alternative React implementation |
| frontend-react-next | Next.js (T3 Stack) | `3002` | 🔬 Experimental | Next.js experimental frontend |
| dev-dashboard | React + Vite | `5174` | 🔬 Experimental | Developer tools dashboard |

All frontends communicate with the backend through the NestJS BFF on port `3001`.

**Frontend Priority:**
1. **Angular** - Primary production frontend
2. **React Vite** - Experimental alternative
3. **React Next.js** - Experimental alternative

## Repository Helpers

| Script | Purpose |
| --- | --- |
| [list-files.sh](list-files.sh) | Repository file listing helper |
| [core/clean-gradle.sh](core/clean-gradle.sh) | Gradle cleanup helper for the backend workspace |
| [ui/bff-nest/run.sh](ui/bff-nest/run.sh) | Run the NestJS BFF |
| [ui/frontend-angular/run.sh](ui/frontend-angular/run.sh) | Run the Angular frontend |
| [ui/frontend-angular/openapi.sh](ui/frontend-angular/openapi.sh) | Angular OpenAPI generation helper |
| [ui/frontend-angular/orval.sh](ui/frontend-angular/orval.sh) | Orval generation helper |
| [ui/frontend-react-vite/run.sh](ui/frontend-react-vite/run.sh) | Run the React Vite frontend |

## Development Notes

- Java services use JDK 21 and Gradle Kotlin DSL.
- Backend modules are listed in [core/settings.gradle.kts](core/settings.gradle.kts).
- Frontend and BFF modules use Node/npm.
- Drools rules are in [core/service/rule-service/src/main/resources/rules](core/service/rule-service/src/main/resources/rules).
- BPMN workflow assets live in [core/service/workflow-service/src/main/resources/bpmn](core/service/workflow-service/src/main/resources/bpmn).
- Shared protobuf definitions live in [proto/omnibus/v1](proto/omnibus/v1), with service-local copies where needed by build tooling.
- Infrastructure and observability are co-located in [infra/](infra/) for simplified local development.

## Documentation Policy

- Keep this root README as the first stop for onboarding, repository map and cross-module flows.
- Keep module-specific commands, TODOs and design notes inside that module's `README.md`.
- Keep service-specific behavior next to the service implementation.
- There is intentionally no `docs/` directory; avoid reintroducing one unless a new documentation format needs it.

## License

MIT. See [LICENSE](LICENSE).
