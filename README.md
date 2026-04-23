# Omnibus

Omnibus is a bank-style monorepo for a bridge bidding platform built around a rule engine.

## Architecture

- `frontend-angular` - Angular UI for entering a hand and viewing the recommended bid
- `bff-nest` - NestJS backend-for-frontend that fronts the domain API
- `services/bidding-engine` - Spring Boot `Java 21` service with `Drools`
- `services/workflow-orchestrator` - Spring Boot service running Camunda workflows around rule governance
- `zeebe` - Camunda 8 workflow engine for BPMN process execution
- `kafka` - event backbone for recommendation and rule update events
- `services/event-archive` - Kafka consumer archiving events into Cassandra
- `cassandra` - durable event history store
- `keycloak` - identity provider for administrator login
- `nginx` - reverse proxy and single external entry point
- `infra` - shared infrastructure notes and placeholders

## Decision Flow

1. The Angular client sends a recommendation request to the Nest BFF.
2. The BFF validates and forwards the request to the Spring backend.
3. The Spring backend computes hand facts and inserts them into Drools.
4. Drools creates candidate bids and the backend returns the best recommendation with an explanation.
5. The backend publishes domain events to Kafka after recommendation and rule update actions.
6. Rule changes from the admin panel go through Camunda before they reach the Drools rule store.
7. NGINX exposes the platform through one external entry point and routes traffic to the right service.

## Local Run

### Spring backend

```bash
cd services/bidding-engine
gradle bootRun
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html`.

### Nest BFF

```bash
cd bff-nest
npm install
npm run start:dev
```

### Angular frontend

```bash
cd frontend-angular
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

Prometheus is configured in [infra/prometheus/prometheus.yml](/Users/kamilzabinski/IdeaProjects/writeonly/Omnibus/infra/prometheus/prometheus.yml:1) and scrapes:

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
