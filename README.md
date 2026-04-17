# Omnibus

Omnibus is a bank-style monorepo for a bridge bidding platform built around a rule engine.

## Architecture

- `frontend-angular` - Angular UI for entering a hand and viewing the recommended bid
- `bff-nest` - NestJS backend-for-frontend that fronts the domain API
- `bidding-engine` - Spring Boot `Java 21` service with `Drools`
- `infra` - shared infrastructure notes and placeholders

## Decision Flow

1. The Angular client sends a recommendation request to the Nest BFF.
2. The BFF validates and forwards the request to the Spring backend.
3. The Spring backend computes hand facts and inserts them into Drools.
4. Drools creates candidate bids and the backend returns the best recommendation with an explanation.

## Local Run

### Spring backend

```bash
cd bidding-engine
mvn spring-boot:run
```

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

## First Supported Scope

The initial Drools ruleset handles simple opening recommendations:

- `PASS` with fewer than 12 HCP
- `1NT` with balanced 15-17 HCP
- `1S` with 5+ spades
- `1H` with 5+ hearts
- `1D` with longer diamonds than clubs
- `1C` otherwise

## Repo Notes

- The previous Kotlin prototype is intentionally not carried forward here.
- This scaffold favors explicit layers and service boundaries over framework shortcuts.
