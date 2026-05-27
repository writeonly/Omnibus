# Rule Service

Drools-based bridge bidding rule service.

## Responsibility

`rule-service` parses bridge hands, builds bidding facts, evaluates bundled and managed Drools rules, and returns bidding recommendations. It also exposes rule administration paths and can publish domain events for audit/archive consumers.

## Local Port

| Interface | Port |
| --- | --- |
| HTTP | `8084` |
| gRPC | `9081` |

## Recommendation Flow

```text
1. User enters a bridge hand in a frontend.
2. Frontend sends a request to the NestJS BFF.
3. BFF calls the backend through the API gateway boundary.
4. API gateway routes to rule-service.
5. rule-service parses the hand into facts.
6. Drools evaluates bundled and managed rules.
7. rule-service returns a recommendation synchronously.
8. rule-service may publish domain events for audit/archive projections.
```

## Main Parts

| Path | Purpose |
| --- | --- |
| `src/main/kotlin/.../api` | REST API controllers and OpenAPI configuration |
| `src/main/kotlin/.../grpc` | gRPC service and mapping code |
| `src/main/kotlin/.../rules` | Drools compilation/evaluation and rule catalog code |
| `src/main/kotlin/.../application` | Managed rule and recommendation application services |
| `src/main/kotlin/.../events` | Domain event publication abstractions |
| [src/main/resources/rules](src/main/resources/rules) | Bundled Drools rules |
| [src/main/resources/META-INF/kmodule.xml](src/main/resources/META-INF/kmodule.xml) | Drools module configuration |

## Development

```bash
cd ../..
./gradlew :rule-service:bootRun
```

The service is also included in [../../docker-compose.yml](../../docker-compose.yml).
