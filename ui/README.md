# UI

This directory contains the frontend and backend-for-frontend modules for Omnibus.

## Modules

| Module | Purpose |
| --- | --- |
| [frontend-angular](frontend-angular/README.md) | Primary frontend |
| [frontend-react](frontend-react/README.md) | Alternative React frontend |
| [dev-dashboard](dev-dashboard/README.md) | Developer dashboard |
| [bff-nest](bff-nest/README.md) | NestJS backend-for-frontend |

## Presentation Flow

```text
Frontend apps
  -> NestJS BFF
  -> API gateway
  -> micro services
```

The BFF shields UI clients from backend topology. In Docker Compose it calls the backend API gateway through the shared `omnibus` network.

## Docker Compose

```bash
docker compose up --build
```

| Component | URL |
| --- | --- |
| Angular frontend container | `http://localhost:4200` |
| NestJS BFF | `http://localhost:3001` |

## Local Development

Use the README in the module you are working on. Common commands include:

```bash
cd bff-nest
npm install
npm run start:dev

cd ../frontend-angular
npm install
npm start

cd ../frontend-react
npm install
npm run start
```

## Scripts

| Script | Purpose |
| --- | --- |
| [bff-nest/run.sh](bff-nest/run.sh) | Run the NestJS BFF |
| [frontend-angular/run.sh](frontend-angular/run.sh) | Run the Angular frontend |
| [frontend-angular/openapi.sh](frontend-angular/openapi.sh) | Generate/update Angular OpenAPI client code |
| [frontend-angular/orval.sh](frontend-angular/orval.sh) | Generate/update Orval client code |
| [frontend-angular/nginx/entrypoint.sh](frontend-angular/nginx/entrypoint.sh) | Frontend container entrypoint |
| [frontend-react/run.sh](frontend-react/run.sh) | Run the React frontend |

## TODO
- Add tRPC
