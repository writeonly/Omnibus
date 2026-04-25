# Omnibus Architecture Documentation

## Project Overview

**Omnibus** is a sophisticated Spring Boot 3.4.3 application developed in Kotlin, designed as an "Effective Expert" system. It integrates multiple rules engines and provides comprehensive REST, GraphQL, and reactive APIs for expert decision-making and business rule processing.

---

## Technology Stack

### Core Framework
- **Spring Boot**: 3.4.3
- **Kotlin**: 2.0.10
- **Java**: JDK 21 (LTS)
- **Build Tool**: Gradle with Kotlin DSL

### Spring Ecosystem
- **spring-boot-starter-web**: Traditional REST API support
- **spring-boot-starter-webflux**: Reactive web framework
- **spring-boot-starter-graphql**: GraphQL API support
- **spring-boot-starter-hateoas**: Hypermedia support
- **spring-boot-starter-data-jpa**: Database persistence
- **Spring Security**: Authentication & Authorization (if configured)

### Rules Engines Integration
1. **Drools**: Version 10.0.0 - Industry-standard rule engine
2. **OpenL Rules**: Version 5.27.10 - Decision tables and rules
3. **RuleBook**: Version 0.12 - Fluent rule engine
4. **Easy Rules**: Version 4.1.0 - Lightweight rule processing
5. **Vavr**: Version 0.10.6 - Functional programming library for rule chains

### Data & ORM
- **Hibernate**: Version 6.6.8.Final
- **Jakarta JPA**: 2.0 specifications
- **H2 Database**: In-memory database for development/testing

### API Documentation & Logging
- **SpringDoc OpenAPI**: 2.5.0 - Swagger/OpenAPI documentation
- **Logbook**: 3.0.0 - HTTP request/response logging

### Parsing & DSL
- **Parsus**: 0.6.1 - Parser combinator library
- **Tribune Core**: 1.2.4 - Domain-specific language support

### Testing & Quality
- **Kotest**: 5.8.0 - Kotlin testing framework with BDD style
- **JaCoCo**: Code coverage analysis
- **ArchUnit**: 1.2.0 - Architecture testing
- **Detekt**: 1.23.7 - Kotlin static analysis
- **Spring Boot Test**: Integration testing

### Additional Libraries
- **Reactor Kotlin Extensions**: Coroutine support for reactive streams
- **Jackson Kotlin Module**: JSON serialization/deserialization
- **Lombok**: Boilerplate reduction (optional)

---

## High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     REST / GraphQL / HATEOAS                │
│                    (OpenAPI Documentation)                   │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│              API Controllers & GraphQL Resolvers              │
│            (Web Layer - HTTP Request Handling)                │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│           Business Logic & Rules Engine Integration          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   Drools     │  │  OpenL Rules │  │  RuleBook    │       │
│  │  Easy Rules  │  │  Vavr DSL    │  │  Functional  │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│         Service Layer & Rule Processing Pipeline            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│         Data Access Layer (JPA/Hibernate)                   │
│              Entity Management & Queries                    │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│            H2 Database / Persistent Storage                  │
└─────────────────────────────────────────────────────────────┘

Side Services:
├─ Request/Response Logging (Logbook)
├─ HTTP API Metrics & Observability
├─ Coroutine-based Async Processing (Reactor)
└─ Parser DSL for Custom Rule Definitions
```

---

## Key Architectural Patterns

### 1. **Multi-Rules Engine Integration**
Omnibus doesn't rely on a single rules engine. Instead, it integrates multiple specialized engines:
- Use **Drools** for complex rule sets
- Use **OpenL Rules** for decision tables
- Use **Easy Rules** for lightweight conditional logic
- Use **RuleBook** for fluent API definitions
- Use **Vavr** for functional composition chains

### 2. **Reactive & Async First**
- WebFlux for non-blocking I/O
- Project Reactor for backpressure handling
- Kotlin Coroutines for readable async code

### 3. **API-First Design**
- REST endpoints with Spring Web
- GraphQL queries for flexible data fetching
- HATEOAS for self-describing hypermedia
- OpenAPI/Swagger for automatic documentation

### 4. **Functional Programming**
- Vavr functional collections and patterns
- Higher-order functions for rule composition
- Immutable data structures

### 5. **Data-Driven Architecture**
- Entity/DTO separation via JPA
- Hibernated-enhanced lazy loading
- H2 in-memory database for fast development cycles

---

## Directory Structure

```
Omnibus/
├── src/
│   ├── main/
│   │   ├── kotlin/pl/writeonly/omnibus/
│   │   │   ├── api/                    # REST & GraphQL endpoints
│   │   │   ├── service/                # Business logic layer
│   │   │   ├── rules/                  # Rules engine wrappers
│   │   │   ├── entity/                 # JPA entities
│   │   │   ├── repository/             # Data access layer
│   │   │   ├── dto/                    # Data transfer objects
│   │   │   ├── config/                 # Spring configuration
│   │   │   └── OmnibusApplication.kt   # Main entry point
│   │   └── resources/
│   │       ├── application.yml         # Configuration
│   │       ├── application-dev.yml     # Dev profile
│   │       ├── graphql/                # GraphQL schemas
│   │       └── rules/                  # Rule definitions
│   └── test/
│       ├── kotlin/                     # Kotest test suites
│       └── resources/                  # Test fixtures & configs
├── build.gradle.kts                    # Gradle build configuration
├── settings.gradle.kts                 # Multi-module settings
├── gradle/                             # Gradle wrapper & plugins
├── detekt.yml                          # Static analysis config
├── HELP.md                             # Getting started guide
└── README.md                           # Project README
```

---

## Core Components

### 1. **OmnibusApplication** (Main Entry Point)
```
pl.writeonly.omnibus.OmnibusApplicationKt
- Bootstraps Spring Boot context
- Initializes rules engines
- Loads configuration profiles
```

### 2. **API Layer**
- **REST Controllers**: Handle HTTP requests, return JSON
- **GraphQL Resolvers**: Process GraphQL queries/mutations
- **HATEOAS Responses**: Include hyperlinks for discoverability

### 3. **Service Layer**
- Business logic orchestration
- Rules engine coordination
- Cross-cutting concerns (logging, transactions)

### 4. **Rules Engines**
Each engine has a dedicated wrapper/adapter:
- `DroolsService`: Manages Drools knowledge bases
- `OpenLRulesService`: Handles decision tables
- `RuleBookEngine`: Fluent rule definitions
- `EasyRulesProcessor`: Lightweight rule execution
- `VavrFunctionChain`: Functional composition

### 5. **Data Access Layer**
- **Repositories**: Spring Data JPA repositories for CRUD
- **Entities**: Hibernate-managed domain models
- **DTOs**: Transfer objects for API contracts

---

## Build & Deployment

### Development Build
```bash
./gradlew clean build
```

### Run Application
```bash
./gradlew bootRun
```

### Code Quality Checks
```bash
gradle detekt --auto-correct
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

### Build Docker Image
```bash
./gradlew bootBuildImage
docker run --rm -p 8080:8080 omnibus:0.0.1-SNAPSHOT
```

### Build Native Executable (GraalVM)
```bash
./gradlew nativeCompile
./build/native/nativeCompile/omnibus
```

### Run Tests in Native Image
```bash
./gradlew nativeTest
```

---

## API Endpoints

### REST API
- **Base URL**: `http://localhost:8080`
- **OpenAPI/Swagger**: `http://localhost:8080/swagger-ui.html`

### GraphQL API
- **Endpoint**: `http://localhost:8080/graphql`
- **Schema**: Auto-generated from domain models
- **GraphQL UI**: Embedded in application

### HATEOAS Links
All responses include hypermedia links for navigation between related resources.

---

## Testing Strategy

### Unit Tests (Kotest)
- Test individual services and business logic
- Use Kotest's BDD-style specifications
- Mock external dependencies

### Integration Tests
- Test Spring context and bean wiring
- Use H2 in-memory database
- Verify rule engine integrations

### Architecture Tests (ArchUnit)
- Enforce layering rules (no circular dependencies)
- Validate package structure
- Prevent unauthorized cross-layer access

### Code Coverage (JaCoCo)
- Minimum coverage requirements
- Generate HTML reports
- Integrate with CI/CD pipelines

---

## Configuration Profiles

### Development (`application-dev.yml`)
- H2 in-memory database
- Verbose logging
- Swagger UI enabled
- Live reload with DevTools

### Production (`application.yml`)
- External database configuration
- Optimized logging levels
- Native image compilation options
- GraalVM reflection configuration

---

## Performance Considerations

1. **Reactive Stack**: WebFlux + Reactor for high throughput
2. **Native Compilation**: GraalVM native image for instant startup
3. **Rule Caching**: Pre-compiled rules for faster execution
4. **Database Indexing**: Proper JPA/Hibernate mapping
5. **Connection Pooling**: HikariCP (via Spring Boot)

---

## Key Commands

| Command | Purpose |
|---------|---------|
| `./gradlew clean build` | Clean build and package application |
| `./gradlew bootRun` | Run application locally |
| `./gradlew test` | Run all unit tests |
| `./gradlew jacocoTestReport` | Generate code coverage report |
| `gradle detekt --auto-correct` | Run code analysis and auto-fix issues |
| `./gradlew bootBuildImage` | Build Docker image |
| `./gradlew nativeCompile` | Compile to native executable |
| `./gradlew nativeTest` | Run tests in native image |

---

## Dependencies & Plugins

### Gradle Plugins
- `kotlin("jvm")` - Kotlin compiler plugin
- `kotlin("plugin.spring")` - Spring compatibility plugin
- `kotlin("plugin.jpa")` - JPA plugin for no-arg constructors
- `org.springframework.boot` - Boot plugin
- `org.hibernate.orm` - Hibernate enhancement plugin
- `org.graalvm.buildtools.native` - Native compilation
- `io.gitlab.arturbosch.detekt` - Static analysis
- `jacoco` - Code coverage

### BOM Management
Optional Vaadin BOM for future UI development (currently commented out).

---

## Future Considerations

1. **Microservices**: Potential split into rule engine, API gateway, and data services
2. **Cloud Deployment**: K8s manifest files, Helm charts
3. **Advanced Caching**: Redis integration for distributed caching
4. **Event Streaming**: Kafka integration for async rule processing
5. **Machine Learning**: Integration with ML models via rules engines
6. **UI Layer**: Vaadin Spring Boot starter (framework included but not active)

---

## Maintenance & Monitoring

- **Static Analysis**: Detekt catches code quality issues
- **Test Coverage**: JaCoCo ensures test adequacy
- **Architecture Validation**: ArchUnit prevents structural degradation
- **API Documentation**: OpenAPI/Swagger keeps docs in sync
- **Request Logging**: Logbook tracks all HTTP interactions

---

## Project Metadata

- **Group**: `pl.writeonly`
- **Version**: `0.0.1-SNAPSHOT`
- **License**: MIT
- **Java Toolchain**: JDK 21+
- **Kotlin Compiler**: 2.0.10
- **Requires Maven Central**: All dependencies via Maven Central

---

**Last Updated**: 2026-04-25
