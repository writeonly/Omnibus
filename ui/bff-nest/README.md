# BFF Nest

NestJS backend-for-frontend for Omnibus UI clients.

## Responsibility

`bff-nest` exposes a frontend-friendly HTTP API and translates UI requests to backend calls. In Docker Compose it talks to the API gateway at `http://api-gateway:8080` on the shared `omnibus` network.

## Local Port

| Interface | Port |
| --- | --- |
| HTTP | `3001` |

## Flow

```text
Angular/React UI
  -> bff-nest
  -> api-gateway
  -> core services
```

## Development

```bash
npm install
npm run start:dev
```

or:

```bash
./run.sh
```

## Main Parts

| Path | Purpose |
| --- | --- |
| `src/bidding` | Bidding endpoints and services |
| `src/common/grpc` | gRPC clients, protobuf loading and transport helpers |
| `proto` | Local protobuf inputs used by the BFF |
