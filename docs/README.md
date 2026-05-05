# Omnibus

Omnibus is a monorepo for a bridge bidding platform built around a rule engine and event-driven architecture.

## Architecture

### Applications (presentation layer)

- `presentation/frontend-angular` – Angular UI for entering a hand and viewing bid recommendations
- `presentation/frontend-react` – React UI (experimental / alternative frontend)
- `presentation/bff-nest` – NestJS backend-for-frontend aggregating domain APIs

### Backend services

- `services/event-archive` – Kafka consumer persisting domain events into Cassandra
- `services/bidding-engine` – Spring Boot (Java 21) service with Drools rule engine
- `services/workflow-orchestrator` – Spring Boot service orchestrating rule governance workflows (Camunda / Zeebe style)

### Infrastructure / integration

- Docker Compose – local orchestration of the full stack
- Outbox relay (`infra/outbox-relay`) – bridges local outbox events to Kafka (if enabled)
- Kafka – event backbone for recommendations and rule updates
- Cassandra – durable event storage for archived domain events
- Keycloak – identity provider for admin authentication
- Prometheus – metrics scraping and monitoring
- Promtail – log shipping to observability stack

## Decision Flow

1. Angular frontend sends a recommendation request to the Nest BFF.
2. BFF validates and forwards the request to `bidding-engine`.
3. `bidding-engine` builds hand facts and evaluates rules in Drools.
4. Drools produces candidate bids and returns the best recommendation with explanation.
5. Domain events are published to Kafka (recommendation + rule updates).
6. Rule changes go through `workflow-orchestrator` before being applied to the rule set.
7. `event-archive` consumes Kafka events and persists them in Cassandra.

## Code Quality & Formatting

### Kotlin (Gradle)

Run all formatters and linters:
```bash
./gradlew format
```

Check formatting without modifying files:
```bash
./gradlew formatCheck
```

Run only Detekt linter:
```bash
./gradlew lint
```

Run all code quality checks (format + lint):
```bash
./gradlew codeQuality
```

### TypeScript (Using npm)

Run linter:
```bash
npm run lint
```

Auto-fix linting issues:
```bash
npm run lint:fix
```

Format code:
```bash
npm run format
```

Check formatting without modifying:
```bash
npm run format:check
```

Run type checking:
```bash
npm run typecheck
```

Run all checks (lint + format + typecheck):
```bash
cd presentation/bff-nest
npm run lint:all
```

## Local Run

### Spring backend

```bash
cd services/bidding-engine
gradle bootRun
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

### Nest BFF

```bash
cd presentation/bff-nest
npm install
npm run start:dev
```

### Angular frontend

```bash
cd presentation/frontend-angular
npm install
npm start
```

Use `Node 20 LTS` for both JavaScript applications. The Spring backend uses `Java 21`.

### Docker Compose

```bash
docker compose up --build
```

### NGINX Gateway

- main entry point: `http://localhost:8088`
- frontend UI: `http://localhost:8088/`
- BFF API: `http://localhost:8088/api/...`
- Swagger UI: `http://localhost:8088/swagger-ui.html`
- workflow Swagger UI: `http://localhost:8088/workflow/swagger-ui.html`
- bidding-engine actuator: `http://localhost:8088/actuator/...`
- workflow actuator: `http://localhost:8088/workflow/actuator/...`
- event-archive actuator: `http://localhost:8088/archive/actuator/...`
- Keycloak via proxy: `http://localhost:8088/keycloak/`
- Prometheus via proxy: `http://localhost:8088/prometheus/`

### Kafka

- Broker for local development: `localhost:29092`
- recommendation topic: `omnibus.recommendation.produced`
- rule update topic: `omnibus.rule.updated`

### Cassandra

- CQL port: `localhost:9042`
- keyspace: `omnibus`
- archived tables:
  - `recommendations_by_day`
  - `rule_updates_by_day`

### Keycloak

- URL: `http://localhost:9090`
- realm: `omnibus`
- frontend client: `omnibus-frontend`
- sample admin user: `bridge-admin / changeit`

The Keycloak bootstrap admin for the server console is `kcadmin / kcadmin`.

### Camunda / Zeebe

- Zeebe gRPC: `localhost:26500`
- Zeebe REST: `http://localhost:8089`
- workflow orchestrator API: `http://localhost:8082`
- workflow publish endpoint: `POST /api/v1/rule-publications`

### Prometheus

- URL: `http://localhost:9091`
- Spring metrics: `http://localhost:8080/actuator/prometheus`
- Keycloak metrics are scraped internally from `/metrics`

## First Supported Scope

The initial Drools ruleset handles simple opening recommendations:

- `PASS` with fewer than 12 HCP
- `1NT` with balanced 15-17 HCP
- `1S` with 5+ spades
- `1H` with 5+ hearts
- `1D` with longer diamonds than clubs
- `1C` otherwise

## Admin Workflow

1. Start the stack with `docker compose up --build`.
2. Open the frontend at `http://localhost:8088`.
3. Click `Login admin` and sign in as `bridge-admin / changeit`.
4. Use the admin panel to list bundled and managed DRL rules.
5. Save a new managed rule from the panel. Nest starts a Camunda BPMN process in `workflow-orchestrator`.
6. Camunda runs the `validate-and-publish-rule` job worker, which calls `bidding-engine`.
7. The backend validates the rule by compiling the full Drools set before accepting it.

Managed rules are loaded together with bundled rules on each recommendation request in the current MVP.

## Monitoring

Prometheus is configured in [infra/prometheus/prometheus.yml](/infra/prometheus/prometheus.yml) and scrapes:

- `bidding-engine` through Spring Boot Actuator Prometheus metrics
- `workflow-orchestrator` through Spring Boot Actuator Prometheus metrics
- `event-archive` through Spring Boot Actuator Prometheus metrics
- `keycloak` through the built-in metrics endpoint

## Event Flow

Kafka is used as an asynchronous event bus next to the bidding engine, not for synchronous UI requests.

- `RecommendationProducedEvent` is emitted after the engine returns a bid
- `RuleUpdatedEvent` is emitted after an admin rule is saved and validated
- `event-archive` consumes both topics and writes a durable history to Cassandra

## Repo Notes

- The previous Kotlin prototype is intentionally not carried forward here.
- This scaffold favors explicit layers and service boundaries over framework shortcuts.
- Managed rules created from the admin panel are stored in `services/bidding-engine/managed-rules`.
