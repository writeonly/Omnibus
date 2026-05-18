# Scripts

This page lists script entry points that currently exist in the repository. There is no root-level `scripts/` directory at the moment, so run each script from the path shown below.

## Repository Helpers

| Script | Purpose |
| --- | --- |
| [../../list-files.sh](../../list-files.sh) | Repository file listing helper |
| [../../services/clean-gradle.sh](../../services/clean-gradle.sh) | Gradle cleanup helper for the services workspace |

## Presentation Scripts

| Script | Purpose |
| --- | --- |
| [../../presentation/bff-nest/run.sh](../../presentation/bff-nest/run.sh) | Run the NestJS BFF |
| [../../presentation/frontend-angular/run.sh](../../presentation/frontend-angular/run.sh) | Run the Angular frontend |
| [../../presentation/frontend-angular/openapi.sh](../../presentation/frontend-angular/openapi.sh) | Angular OpenAPI generation helper |
| [../../presentation/frontend-angular/orval.sh](../../presentation/frontend-angular/orval.sh) | Orval generation helper |
| [../../presentation/frontend-angular/nginx/entrypoint.sh](../../presentation/frontend-angular/nginx/entrypoint.sh) | Frontend container entrypoint |
| [../../presentation/frontend-react/run.sh](../../presentation/frontend-react/run.sh) | Run the React frontend |

## Generated Or Local Scripts

| Script | Note |
| --- | --- |
| [../../presentation/frontend-angular/src/generated/openapi/git_push.sh](../../presentation/frontend-angular/src/generated/openapi/git_push.sh) | Generated OpenAPI helper; treat as generated output unless regenerating the client |
