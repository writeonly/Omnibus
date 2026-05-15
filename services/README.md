# Omnibus – Bridge Bidding Platform with Rule Engine

**Omnibus** is a sophisticated monorepo for a bridge bidding platform built around a Drools rule engine and event-driven architecture. It provides AI-powered opening bid recommendations using an extensible rule system managed through an admin workflow.

## 🎯 Project Overview

- **Domain**: Bridge bidding recommendation system
- **Architecture**: Event-driven microservices with rule engine orchestration
- **Tech Stack**: Spring Boot 3.4.3, Kotlin, TypeScript, NestJS, Angular
- **Database**: Cassandra (events), H2 (dev)
- **Messaging**: Kafka (event bus)
- **Orchestration**: Zeebe (workflow management)
- **License**: MIT

---

## 🏗️ Architecture

### Applications (Presentation Layer)

| Component | Technology | Purpose |
|-----------|-----------|---------|
| `presentation/frontend-angular` | Angular | Primary UI for entering hands and viewing bid recommendations |
| `presentation/frontend-react` | React | Experimental alternative frontend |
| `presentation/bff-nest` | NestJS | Backend-for-frontend aggregating domain APIs with validation |

### Backend Services (Business Logic)

| Service | Technology | Responsibility |
|---------|-----------|-----------------|
| `services/rule-engine` | Spring Boot + Drools | Core rule engine evaluating hands and producing bids |
| `services/workflow-engine` | Spring Boot + Zeebe | Manages admin rule creation/validation workflows |
| `services/event-archive` | Spring Boot + Kafka | Kafka consumer persisting domain events into Cassandra |

### Infrastructure & Integration

| Component | Role |
|-----------|------|
| **Kafka** | Event backbone for recommendations and rule updates |
| **Cassandra** | Durable storage for archived domain events |
| **Keycloak** | Identity provider for admin authentication |
| **Zeebe** | Workflow orchestration (Camunda BPMN style) |
| **Prometheus** | Metrics collection and monitoring |
| **Promtail** | Log shipping to observability stack |
| **Docker Compose** | Local orchestration of full stack |
| **Outbox Relay** | Bridges local outbox events to Kafka (optional) |

---

## 🔄 Decision Flow

### Typical Recommendation Request

```
1. User enters hand in Angular UI
   ↓
2. Angular → Nest BFF (HTTP/REST)
   ↓
3. BFF validates input & forwards to rule-engine
   ↓
4. rule-engine extracts hand facts
   ↓
5. Drools rule engine evaluates facts
   ↓
6. Engine produces candidate bids + explanation
   ↓
7. Recommendation → Kafka → RecommendationProducedEvent
   ↓
8. event-archive consumes & persists to Cassandra
   ↓
9. Response flows back to frontend
```

### Admin Rule Publication Workflow

```
1. Admin logs in (bridge-admin/changeit via Keycloak)
   ↓
2. Admin panel lists bundled & managed DRL rules
   ↓
3. Admin saves new rule → POST /api/v1/rule-publications
   ↓
4. Nest BFF triggers Zeebe BPMN process
   ↓
5. Zeebe orchestrates: validate-and-publish-rule job
   ↓
6. Job worker calls rule-engine with full ruleset
   ↓
7. rule-engine validates by compiling Drools
   ↓
8. If valid → RuleUpdatedEvent to Kafka → Cassandra
   ↓
9. New rule loaded on next recommendation request
```

---

## 🚀 Quick Start

### Prerequisites

- **Java**: JDK 21+
- **Node.js**: 20 LTS
- **Docker & Docker Compose**: Latest
- **Gradle**: 8.x (included via wrapper)

### Option 1: Docker Compose (Recommended)

```bash
# Start entire stack (frontend, all services, infra)
docker compose up --build

# Access services via NGINX gateway at http://localhost:8088
```

**Service URLs** (via NGINX reverse proxy `http://localhost:8088`):

| Service | URL |
|---------|-----|
| Frontend UI | `http://localhost:8088/` |
| BFF API | `http://localhost:8088/api/...` |
| Bidding Engine Swagger | `http://localhost:8088/swagger-ui.html` |
| Workflow Swagger | `http://localhost:8088/workflow/swagger-ui.html` |
| Bidding Engine Actuator | `http://localhost:8088/actuator/...` |
| Workflow Actuator | `http://localhost:8088/workflow/actuator/...` |
| Event Archive Actuator | `http://localhost:8088/archive/actuator/...` |
| Keycloak Admin | `http://localhost:8088/keycloak/` |
| Prometheus Metrics | `http://localhost:8088/prometheus/` |

### Option 2: Local Development (Individual Services)

#### 2a. Bidding Engine (Spring Boot)

```bash
cd services/rule-engine
./gradlew bootRun
```

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- Actuator: `http://localhost:8080/actuator/health`

#### 2b. Nest BFF

```bash
cd presentation/bff-nest
npm install
npm run start:dev
```

- API: `http://localhost:3000`
- Hot reload enabled

#### 2c. Angular Frontend

```bash
cd presentation/frontend-angular
npm install
npm start

# Alternative (with proxy config):
ng serve --proxy-config proxy.conf.json --verbose
```

- App: `http://localhost:4200`
- Development server with live reload

#### 2d. React Frontend (Experimental)

```bash
cd presentation/frontend-react
npm install
npm start
```

---

## 🔧 Infrastructure Services (Docker)

### Kafka

```
- Broker: localhost:29092
- Topics:
  - omnibus.recommendation.produced
  - omnibus.rule.updated
```

### Cassandra

```
- CQL Port: localhost:9042
- Keyspace: omnibus
- Tables:
  - recommendations_by_day
  - rule_updates_by_day
```

### Keycloak

```
- URL: http://localhost:9090
- Realm: omnibus
- Frontend Client: omnibus-frontend
- Sample Admin: bridge-admin / changeit
- Server Admin: kcadmin / kcadmin
```

### Zeebe Orchestration

```
- gRPC: localhost:26500
- REST: http://localhost:8089
- Workflow Engine: http://localhost:8083
- Rule Publication: POST /api/v1/rule-publications
```

### Prometheus

```
- URL: http://localhost:9091
- Scrapes:
  - rule-engine: /actuator/prometheus
  - workflow-engine: /actuator/prometheus
  - event-archive: /actuator/prometheus
  - keycloak: /metrics
```

---

## 🎓 Supported Bid Scope (MVP)

The initial Drools ruleset handles opening recommendations for:

```
Fewer than 12 HCP      → PASS
Balanced 15-17 HCP     → 1NT
5+ Spades              → 1S
5+ Hearts              → 1H
Longer Diamonds        → 1D
Otherwise              → 1C
```

Future releases will expand to intermediate/advanced bidding conventions.

---

## 👨‍💼 Admin Workflow

### Setup

1. Start stack: `docker compose up --build`
2. Open frontend: `http://localhost:8088`
3. Click "Login admin"
4. Sign in: `bridge-admin / changeit`

### Creating Custom Rules

1. Navigate to admin panel
2. View bundled rules and previously saved managed rules
3. Write or modify a DRL rule
4. Save rule → Zeebe BPMN process triggered
5. Workflow validates rule in full Drools context
6. If valid → deployed and available immediately

### Current Limitations

- Managed rules stored in `services/rule-engine/managed-rules`
- Rules loaded per request (not cached server-side yet)
- Future: hot-reload and rule versioning

---

## 📊 Code Quality & Formatting

### Backend (Kotlin + Gradle)

```bash
# Format all Kotlin code
./gradlew format

# Check formatting without changes
./gradlew formatCheck

# Run Detekt linter
./gradlew lint

# Run all quality checks
./gradlew codeQuality

# Build with tests
./gradlew clean build

# Run tests with coverage
./gradlew test jacocoTestReport
```

### Frontend (TypeScript + npm)

```bash
cd presentation/bff-nest

# Lint
npm run lint

# Auto-fix linting issues
npm run lint:fix

# Format code
npm run format

# Check formatting without changes
npm run format:check

# Type checking
npm run typecheck

# Run all checks
npm run lint:all
```

---

## 📈 Monitoring & Observability

### Metrics

Prometheus scrapes from all Spring Boot services via Actuator endpoints:

```yaml
# View metrics
GET http://localhost:8088/actuator/prometheus
```

### Logging

Promtail ships logs to observability stack. Configure log levels in:

- `services/rule-engine/src/main/resources/application.yml`
- `services/workflow-engine/src/main/resources/application.yml`
- `services/event-archive/src/main/resources/application.yml`

### Health Checks

```bash
# Bidding Engine
curl http://localhost:8082/actuator/health

# Workflow Engine
curl http://localhost:8083/actuator/health

# Event Archive
curl http://localhost:8084/actuator/health
```

---

## 🔄 Event Architecture

### Event Types

| Event | Topic | Producer | Consumer |
|-------|-------|----------|----------|
| `RecommendationProducedEvent` | `omnibus.recommendation.produced` | rule-engine | event-archive |
| `RuleUpdatedEvent` | `omnibus.rule.updated` | rule-engine | event-archive |

### Event Flow

```
Synchronous (HTTP):
  Frontend → BFF → Bidding Engine → Response

Asynchronous (Kafka):
  Bidding Engine → RecommendationProducedEvent → event-archive → Cassandra
  Workflow Engine → RuleUpdatedEvent → event-archive → Cassandra
```

---

## 📂 Repository Structure

```
Omnibus/
├── docker-compose.yml                  # Full stack orchestration
├── build.gradle.kts                    # Root Gradle config
├── settings.gradle.kts                 # Multi-module setup
│
├── services/
│   ├── rule-engine/                 # Spring Boot + Drools rule engine
│   │   ├── src/main/kotlin/
│   │   ├── src/main/resources/rules/   # DRL rule definitions
│   │   └── managed-rules/              # Admin-created rules
│   ├── workflow-engine/          # Spring Boot + Zeebe
│   └── event-archive/                  # Spring Boot + Cassandra consumer
│
├── presentation/
│   ├── frontend-angular/               # Angular SPA
│   ├── frontend-react/                 # React experimental frontend
│   └── bff-nest/                       # NestJS backend-for-frontend
│
├── infra/
│   ├── prometheus/                     # Prometheus config
│   ├── keycloak/                       # Keycloak realm + users
│   ├── cassandra/                      # Cassandra schema
│   ├── kafka/                          # Kafka topics
│   └── outbox-relay/                   # Optional local-to-Kafka bridge
│
└── docs/
    ├── README.md                       # This file (comprehensive guide)
    ├── ARCHITECTURE.md                 # Deep-dive architecture
    └── LICENSE                         # MIT License
```

---

## 🔐 Security

- **Authentication**: Keycloak OAuth2/OpenID Connect
- **Admin Role**: Required for rule management (`bridge-admin`)
- **API Signing**: Web commit signoff required on main branch
- **API Keys**: (Future) service-to-service authentication

---

## 🧪 Testing Strategy

### Unit Tests (Kotest)

```bash
./gradlew test
```

### Integration Tests

Services include Spring integration tests with embedded H2 database.

### Architecture Tests (ArchUnit)

Enforces layering rules and prevents circular dependencies.

### Code Coverage (JaCoCo)

```bash
./gradlew jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

---

## 🛠️ Development Tips

### Hot Reload

- **Angular**: Built-in live reload on `ng serve`
- **NestJS**: Enabled with `npm run start:dev`
- **Spring Boot**: Use DevTools (included)

### Debugging

- **Bidding Engine**: Access Swagger UI at `http://localhost:8080/swagger-ui.html`
- **Workflow Engine**: REST API at `http://localhost:8082`
- **Frontend**: Browser DevTools (F12)

### Database Reset

```bash
# Reset Cassandra (in docker-compose)
docker compose down -v
docker compose up

# Or manually:
docker exec omnibus-cassandra-1 cqlsh -e "DROP KEYSPACE omnibus;"
```

### Kafka Debugging

```bash
# Consume from topic
docker exec omnibus-kafka-1 kafka-console-consumer \
  --bootstrap-server localhost:29092 \
  --topic omnibus.recommendation.produced \
  --from-beginning
```

---

## 📝 Design Principles

- **Event-Driven**: Asynchronous communication via Kafka
- **Layered Architecture**: Clear separation of concerns (API → Service → Rules → Data)
- **Rule Engine Agnostic**: Extensible to multiple rule engines
- **API-First**: REST, GraphQL, and HATEOAS support
- **Observable**: Prometheus metrics, request logging, health checks
- **Testable**: Unit, integration, and architecture tests
- **Explicit Over Implicit**: Favors clear interfaces over framework magic

---

## 🚧 Future Roadmap

- [ ] Intermediate/advanced bidding conventions
- [ ] Conventional 2/1 game forcing
- [ ] Slam bidding logic
- [ ] Rule versioning and deployment strategies
- [ ] Machine learning integration for bid prediction
- [ ] Kubernetes deployment manifests
- [ ] GraphQL API expansion
- [ ] Real-time collaborative bidding

---

## 📚 Documentation

- **Architecture Deep-Dive**: See [ARCHITECTURE.md](docs/ARCHITECTURE.md)
- **Admin Guide**: See admin workflow section above
- **API Reference**: Swagger UI available after startup

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Ensure code quality: `./gradlew codeQuality && npm run lint:all`
4. Submit a pull request

Web commit signoff is required on the main branch.

---

## 📄 License

This project is licensed under the **MIT License** – see [LICENSE](docs/LICENSE) for details.

---

## 📞 Support & Questions

For issues, questions, or contributions, please open a GitHub issue in the [writeonly/Omnibus](https://github.com/writeonly/Omnibus) repository.

---

**Last Updated**: May 2026  
**Status**: Active Development (MVP Phase)

## TODO
- Use Kure Potlin
- Use vavr
- Arrow

