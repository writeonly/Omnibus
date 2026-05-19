# Presentation

This directory contains the frontend and backend-for-frontend modules for Omnibus.

## Modules

| Module | Purpose |
| --- | --- |
| `frontend-angular` | Primary frontend |
| `frontend-react` | Alternative React frontend |
| `dev-dashboard` | Developer dashboard |
| `bff-nest` | NestJS backend-for-frontend |

## Docker Compose

```bash
docker compose up --build
```

| Component | URL |
| --- | --- |
| Angular frontend container | `http://localhost:4300` |
| NestJS BFF | `http://localhost:3000` |

The BFF is configured to call the backend API gateway at `http://api-gateway:8080` inside the shared Docker network.

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
