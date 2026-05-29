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

The compose file creates or uses the shared Docker network named `omnibus`.

## Core Ports

| Service | Port/URL |
| --- | --- |
| PostgreSQL | `localhost:5432` |
| Keycloak | `http://localhost:9000` |
| Redis | `localhost:6379` |
| RabbitMQ | `localhost:5672` |
| RabbitMQ Management | `http://localhost:15672` |
| Kafka external listener | `localhost:29092` with `first` profile |
| Kafka UI | `http://localhost:9094` with `first` profile |
| Redpanda external listener | `localhost:9092` with `follow` profile |
| Cassandra/Scylla | `localhost:9042` with matching profile |
| Elasticsearch/OpenSearch | `http://localhost:9200` with matching profile |
| Kibana/OpenSearch Dashboards | `http://localhost:5601` with matching profile |
| NGINX legacy gateway | `http://localhost:8000` with `legacy` profile |

## Data And State

| Store | Current role |
| --- | --- |
| PostgreSQL | Service databases, auth-related state and outbox tables |
| Redis | Cache and token blacklist support |
| RabbitMQ | Local messaging experiments and integration support |
| Kafka/Redpanda | Domain events, audit/archive projections, config bus use cases |
| Cassandra/Scylla | Event/audit/projection storage path |
| MongoDB/FerretDB | Audit/search-oriented storage experiments |
| Elasticsearch/OpenSearch | Search and log/audit exploration |

## Local Assets

| Path | Purpose |
| --- | --- |
| [postgres](postgres) | PostgreSQL initialization scripts |
| [keycloak](keycloak) | Imported Keycloak realm configuration |
| [cassandra](cassandra) | Cassandra initialization scripts |
| [nginx](nginx) | Legacy NGINX gateway configuration |
| [outbox-relay](outbox-relay) | Node-based outbox relay helper |

# Observability

This directory contains the local observability stack for Omnibus.

## Docker Compose

```bash
docker compose up --build
```

The stack uses the shared `omnibus` Docker network.

## Components

| Component | URL/Port | Purpose |
| --- | --- | --- |
| Prometheus | `http://localhost:9090` | Metrics collection |
| Loki | `http://localhost:3100` | Log storage |
| Promtail | internal | Log shipping to Loki |
| Grafana | `http://localhost:3001` | Dashboards and exploration |

## Configuration

| Path | Purpose |
| --- | --- |
| [prometheus/prometheus.yml](prometheus/prometheus.yml) | Prometheus scrape configuration |
| [promtail/promtail.yml](promtail/promtail.yml) | Promtail log shipping configuration |

