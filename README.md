# Omnibus

Omnibus is a bridge bidding platform organized as a monorepo. The project combines JVM backend services, a NestJS backend-for-frontend, frontend applications, and local infrastructure for identity, messaging, persistence, and observability.

Central, cross-project information lives in this root README. Details that belong to one part of the system live in the README next to that code.

## Project Map

| Area | Path | Purpose |
| --- | --- | --- |
| Backend | [api/README.md](api/README.md) | Spring Boot/Kotlin services, HTTP/gRPC APIs, rule processing, workflow, auth, audit, config and discovery |
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

## Local Development Setup

### Prerequisites

Before starting, ensure you have the following installed:

- **Docker & Docker Compose** v2.0+ ([Install Docker Desktop](https://www.docker.com/products/docker-desktop))
- **Node.js** 20+ ([Install Node.js](https://nodejs.org/))
- **Java JDK** 21 ([Install JDK 21](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html))
- **Git** ([Install Git](https://git-scm.com/))

Verify installations:

```bash
docker --version
docker compose version
node --version
npm --version
java -version
git --version
```

### Quick Start (5 minutes)

If you just want to see the system running:

```bash
# Clone the repository
git clone https://github.com/writeonly/Omnibus.git
cd Omnibus

# Terminal 1: Start infrastructure (PostgreSQL, Keycloak, Redis, etc.)
cd infra
docker compose up --build

# Terminal 2: Start backend services (in a new terminal)
cd core
docker compose up --build

# Terminal 3: Start frontend and BFF (in a new terminal)
cd ui
docker compose up --build
```

Then access:
- **Angular Frontend**: http://localhost:4200
- **NestJS BFF**: http://localhost:3001
- **Keycloak Admin**: http://localhost:9000 (admin / kcadmin)
- **Grafana**: http://localhost:3000

### Full Local Development Setup

For full development with all features and optional services:

#### Step 1: Infrastructure Stack

```bash
cd infra

# Start basic infrastructure (PostgreSQL, Keycloak, Redis, RabbitMQ, etc.)
docker compose up --build

# Optional: Start with additional data stores (Kafka, Cassandra, Elasticsearch)
docker compose --profile first up --build

# Optional: Alternative stack (Redpanda, Scylla, OpenSearch instead of Kafka/Cassandra/Elasticsearch)
docker compose --profile follow up --build

# Optional: Include legacy NGINX gateway
docker compose --profile legacy up --build
```

**Wait for services to be healthy** (check logs for "ready" messages):
- PostgreSQL: `database system is ready to accept connections`
- Keycloak: `Keycloak X.X.X started`
- Redis: `Ready to accept connections`

#### Step 2: Backend Services

In a new terminal:

```bash
cd core

# Start all backend services
docker compose up --build

# Or run individual services locally for development
./gradlew :rule-service:bootRun
./gradlew :workflow-service:bootRun
./gradlew :user-service:bootRun
./gradlew :auth-service:bootRun
```

**Wait for services to start**:
- eureka-server: http://localhost:8761
- API Gateway: http://localhost:8080
- Each service logs startup completion

#### Step 3: Frontend and BFF

In a new terminal:

```bash
cd ui

# Start all frontend and BFF services
docker compose up --build

# Or run individual services locally
cd bff-nest
npm install
npm run start:dev

# In another terminal, run the frontend
cd ../frontend-angular
npm install
npm start
```

**Access services:**
- Angular Frontend: http://localhost:4200
- NestJS BFF: http://localhost:3001
- React Vite (if running): http://localhost:5173
- React Next.js (if running): http://localhost:3002

### Verification Checklist

After startup, verify everything is working:

```bash
# Check services are responding
curl http://localhost:9000/health  # Keycloak
curl http://localhost:8080/actuator/health  # API Gateway
curl http://localhost:3001/health  # NestJS BFF

# Check database
PGPASSWORD=omnibus psql -h localhost -U omnibus -d omnibus -c "SELECT version();"

# Check Redis
redis-cli -p 6379 ping  # Should respond with PONG

# Check RabbitMQ
curl http://localhost:15672/api/vhosts  # Username/password: omnibus/omnibus
```

### Main Local Ports

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
| auth-service | `http://localhost:8082` |
| rule-service | `http://localhost:8086` |
| workflow-service | `http://localhost:8087` |
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

### Development Workflows

#### Frontend Development (Angular)

```bash
cd ui/frontend-angular

# Install dependencies
npm install

# Start development server
npm start
# or
./run.sh

# Build for production
npm run build

# Run tests
npm run test

# Run e2e tests
npm run e2e
```

#### Frontend Development (React Vite)

```bash
cd ui/frontend-react-vite

# Install dependencies
npm install

# Start development server
npm run dev

# Build for production
npm run build
```

#### BFF Development (NestJS)

```bash
cd ui/bff-nest

# Install dependencies
npm install

# Start development with hot reload
npm run start:dev

# Build for production
npm run build

# Run tests
npm run test
```

#### Backend Development (Spring Boot)

```bash
cd core

# Build all services
./gradlew clean build

# Run specific service
./gradlew :rule-service:bootRun
./gradlew :user-service:bootRun
./gradlew :auth-service:bootRun

# Run all tests
./gradlew test

# Clean build artifacts
./clean-gradle.sh
```

### Docker Compose Profiles

The infrastructure supports different profiles for different use cases:

```bash
cd infra

# Default: Core services only (PostgreSQL, Redis, RabbitMQ, Keycloak, etc.)
docker compose up --build

# Profile 'first': Add Kafka, Cassandra, Elasticsearch, MongoDB, Kibana
docker compose --profile first up --build

# Profile 'follow': Add Redpanda, Scylla, OpenSearch, FerretDB
docker compose --profile follow up --build

# Profile 'legacy': Add NGINX gateway
docker compose --profile legacy up --build

# Combine profiles
docker compose --profile first --profile legacy up --build
```

### Troubleshooting

#### Port Already in Use

```bash
# Find process using port (e.g., 4200)
lsof -i :4200

# Kill process
kill -9 <PID>

# Or use Docker to see which container uses the port
docker ps --all | grep 4200
```

#### Service Connection Issues

```bash
# Check if services are running
docker ps

# Check service logs
docker compose logs <service-name>

# Verify network connectivity
docker network ls
docker network inspect omnibus
```

#### Database Issues

```bash
# Connect to PostgreSQL
PGPASSWORD=omnibus psql -h localhost -U omnibus -d omnibus

# Check migrations
SELECT * FROM flyway_schema_history;

# Restart and clean database
docker compose down -v  # Remove volumes
docker compose up --build
```

#### Frontend Proxy Issues

If frontend can't reach BFF, check:

```bash
# Verify BFF is running
curl http://localhost:3001/health

# Check Angular proxy configuration
cat ui/frontend-angular/proxy.conf.js

# Check BFF environment variables
docker logs <bff-container-id>
```

#### Memory Issues

If services fail to start, increase Docker memory:

1. **Docker Desktop**: Preferences → Resources → Memory (set to 4GB+)
2. **Or limit specific services** in docker-compose.yml:
   ```yaml
   services:
     postgres:
       mem_limit: 1g  # Increase from 512m
   ```

### Common Commands

```bash
# Stop all services
docker compose down

# Remove volumes (careful - deletes data!)
docker compose down -v

# View logs
docker compose logs -f <service-name>

# Rebuild specific service
docker compose up --build <service-name>

# Run migrations
./gradlew :core:flywayMigrate

# Format code
./gradlew spotlessApply

# Run full test suite
./gradlew test
npm test
```

### IDE Setup

#### VS Code

1. Install extensions:
   - Angular Language Service
   - NestJS
   - Spring Boot Extension Pack
   - Docker

2. Configure debugging in `.vscode/launch.json`

#### IntelliJ IDEA

1. Import project as Gradle project
2. Configure JDK 21 in Project Settings
3. Enable annotation processing for Lombok (if used)
4. Run services via Gradle tasks

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
