# Omnibus Documentation

This folder contains cross-project documentation. The repository root [README](../README.md) is the main entry point for setup, repository layout, and day-to-day navigation.

## Documents

| Document | Purpose |
| --- | --- |
| [../README.md](../README.md) | Main project overview, local startup, component map |
| [ARCHITECTURE.md](ARCHITECTURE.md) | Architecture deep dive and service flows |
| [scripts/README.md](scripts/README.md) | Index of existing repository scripts |
| [LICENSE](LICENSE) | MIT license |

## Module READMEs

Module-specific notes intentionally stay with their modules:

| Area | README |
| --- | --- |
| Backend services | [../services/README.md](../services/README.md) |
| Presentation apps | [../presentation/README.md](../presentation/README.md) |
| Infrastructure | [../infra/README.md](../infra/README.md) |
| API gateway | [../services/core/api-gateway/README.md](../services/core/api-gateway/README.md) |
| Rule engine | [../services/core/rule-engine/README.md](../services/core/rule-engine/README.md) |
| Workflow engine | [../services/core/workflow-engine/README.md](../services/core/workflow-engine/README.md) |
| Auth server | [../services/core/auth-server/README.md](../services/core/auth-server/README.md) |
| User service | [../services/core/user-service/README.md](../services/core/user-service/README.md) |
| Audit service | [../services/core/audit-service/README.md](../services/core/audit-service/README.md) |
| Config server | [../services/core/config-server/README.md](../services/core/config-server/README.md) |
| NestJS BFF | [../presentation/bff-nest/README.md](../presentation/bff-nest/README.md) |
| Angular frontend | [../presentation/frontend-angular/README.md](../presentation/frontend-angular/README.md) |
| React frontend | [../presentation/frontend-react/README.md](../presentation/frontend-react/README.md) |
| Developer dashboard | [../presentation/dev-dashboard/README.md](../presentation/dev-dashboard/README.md) |

## Documentation Ownership

- Put onboarding and local startup changes in the root [README](../README.md).
- Put architecture changes that affect several modules in [ARCHITECTURE.md](ARCHITECTURE.md).
- Put implementation details and local TODOs in the module README closest to the code.
