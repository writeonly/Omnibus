# Omnibus

Omnibus is a bank-style monorepo for a bridge bidding platform built around a rule engine.

## Architecture

- `frontend-angular` - Angular UI for entering a hand and viewing the recommended bid
- `bff-nest` - NestJS backend-for-frontend that fronts the domain API
- `bidding-engine` - Spring Boot `Java 21` service with `Drools`
- `kafka` - event backbone for recommendation and rule update events
- `keycloak` - identity provider for administrator login
- `infra` - shared infrastructure notes and placeholders

## Decision Flow

1. The Angular client sends a recommendation request to the Nest BFF.
2. The BFF validates and forwards the request to the Spring backend.
3. The Spring backend computes hand facts and inserts them into Drools.
4. Drools creates candidate bids and the backend returns the best recommendation with an explanation.
5. The backend publishes domain events to Kafka after recommendation and rule update actions.

## Local Run

### Spring backend

```bash
cd bidding-engine
mvn spring-boot:run
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

### Kafka

- Broker for local development: `localhost:29092`
- recommendation topic: `omnibus.recommendation.produced`
- rule update topic: `omnibus.rule.updated`

### Keycloak

- URL: `http://localhost:9090`
- realm: `omnibus`
- frontend client: `omnibus-frontend`
- sample admin user: `bridge-admin / changeit`

The Keycloak bootstrap admin for the server console is `kcadmin / kcadmin`.

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
2. Open the frontend at `http://localhost:4200`.
3. Click `Login admin` and sign in as `bridge-admin / changeit`.
4. Use the admin panel to list bundled and managed DRL rules.
5. Save a new managed rule from the panel. The backend validates the rule by compiling the full Drools set before accepting it.

Managed rules are loaded together with bundled rules on each recommendation request in the current MVP.

## Monitoring

Prometheus is configured in [infra/prometheus/prometheus.yml](/Users/kamilzabinski/IdeaProjects/writeonly/Omnibus/infra/prometheus/prometheus.yml:1) and scrapes:

- `bidding-engine` through Spring Boot Actuator Prometheus metrics
- `keycloak` through the built-in metrics endpoint

## Event Flow

Kafka is used as an asynchronous event bus next to the bidding engine, not for synchronous UI requests.

- `RecommendationProducedEvent` is emitted after the engine returns a bid
- `RuleUpdatedEvent` is emitted after an admin rule is saved and validated

## Repo Notes

- The previous Kotlin prototype is intentionally not carried forward here.
- This scaffold favors explicit layers and service boundaries over framework shortcuts.
- Managed rules created from the admin panel are stored in `bidding-engine/managed-rules`.
