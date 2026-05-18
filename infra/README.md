# Infrastructure

This directory contains local shared infrastructure for Omnibus.

## Docker Compose

```bash
docker compose up --build
```

Optional profiles:

| Profile | Adds |
| --- | --- |
| `first` | Kafka, Kafka UI, Cassandra, Elasticsearch, Kibana, MongoDB |
| `follow` | Redpanda, Scylla, OpenSearch, OpenSearch Dashboards, FerretDB |
| `legacy` | NGINX gateway |

## Core Ports

| Service | Port/URL |
| --- | --- |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9090` |
| Redis | `localhost:6379` |
| RabbitMQ Management | `http://localhost:15672` |
| Kafka external listener | `localhost:29092` with `first` profile |
| Kafka UI | `http://localhost:9002` with `first` profile |
| Cassandra/Scylla | `localhost:9042` with matching profile |
| NGINX legacy gateway | `http://localhost:8000` with `legacy` profile |

The compose file creates/uses the shared Docker network named `omnibus`.
