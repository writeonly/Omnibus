# Workflow Engine

Rule-publication workflow service.

## Responsibility

`workflow-engine` orchestrates rule publication requests. It receives publication submissions, starts the publication workflow, and validates candidate rules through `rule-engine` before publication.

## Local Port

| Interface | Port |
| --- | --- |
| HTTP | `8084` |
| gRPC | `9082` |

## Rule Publication Flow

```text
1. Admin submits a rule publication request.
2. Request enters the backend through the BFF/gateway boundary.
3. workflow-engine starts the publication workflow.
4. workflow-engine calls rule-engine to validate the full ruleset.
5. rule-engine compiles the candidate Drools rules.
6. Valid rules can be persisted as managed rules.
7. A rule update event can be published for downstream consumers.
```

## Main Parts

| Path | Purpose |
| --- | --- |
| `src/main/kotlin/.../api` | REST API controllers and OpenAPI configuration |
| `src/main/kotlin/.../application` | Workflow application service and worker |
| `src/main/kotlin/.../domain` | Rule publication request/submission models |
| `src/main/kotlin/.../grpc` | gRPC service, client and mapping code |
| [src/main/resources/bpmn](src/main/resources/bpmn) | BPMN workflow assets |

## Development

```bash
cd ../..
./gradlew :workflow-engine:bootRun
```

The service is also included in [../../docker-compose.yml](../../docker-compose.yml).

## TODO

- Add persistent Kogito process instance storage when workflow history is required.
