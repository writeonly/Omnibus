# Infrastructure and Observability

This directory contains local shared infrastructure and observability stack for Omnibus.

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

## Services and Ports

### Core Infrastructure Services

| Service | Port/URL | Purpose |
| --- | --- | --- |
| PostgreSQL | `localhost:5432` | Service databases, auth state, outbox tables |
| Keycloak | `http://localhost:9000` | Identity and access management |
| Redis | `localhost:6379` | Cache and token blacklist support |
| RabbitMQ | `localhost:5672` | Local messaging experiments |
| RabbitMQ Management | `http://localhost:15672` | RabbitMQ admin interface |

### Optional Data Stores (requires profiles)

| Service | Port | Profile | Purpose |
| --- | --- | --- | --- |
| Kafka | `localhost:29092` | `first` | Domain events, audit projections, config bus |
| Kafka UI | `http://localhost:9094` | `first` | Kafka monitoring |
| Redpanda | `localhost:9092` | `follow` | Alternative to Kafka |
| Cassandra/Scylla | `localhost:9042` | matching | Event/audit/projection storage |
| Elasticsearch | `http://localhost:9200` | `first` | Search and log/audit exploration |
| OpenSearch | `http://localhost:9200` | `follow` | Elasticsearch alternative |
| Kibana | `http://localhost:5601` | `first` | Elasticsearch dashboards |
| OpenSearch Dashboards | `http://localhost:5601` | `follow` | OpenSearch dashboards |
| MongoDB | `localhost:27017` | `first` | Audit/search-oriented storage experiments |
| FerretDB | `localhost:27017` | `follow` | MongoDB alternative |

### Observability Stack

| Component | URL/Port | Purpose |
| --- | --- | --- |
| Prometheus | `http://localhost:9090` | Metrics collection and time-series database |
| Loki | `http://localhost:3100` | Log aggregation and storage |
| Promtail | internal | Log shipper to Loki |
| Grafana | `http://localhost:3001` | Metrics dashboards and log exploration |

### Legacy Services

| Service | Port/URL | Profile | Purpose |
| --- | --- | --- | --- |
| NGINX | `http://localhost:8000` | `legacy` | Legacy API gateway |

## Data Storage Roles

| Store | Current Role |
| --- | --- |
| PostgreSQL | Service databases, auth-related state and outbox tables |
| Redis | Cache and token blacklist support |
| RabbitMQ | Local messaging experiments and integration support |
| Kafka/Redpanda | Domain events, audit/archive projections, config bus use cases |
| Cassandra/Scylla | Event/audit/projection storage path |
| MongoDB/FerretDB | Audit/search-oriented storage experiments |
| Elasticsearch/OpenSearch | Search and log/audit exploration |

## Local Configuration Assets

| Path | Purpose |
| --- | --- |
| [postgres](postgres) | PostgreSQL initialization scripts |
| [keycloak](keycloak) | Imported Keycloak realm configuration |
| [cassandra](cassandra) | Cassandra initialization scripts |
| [nginx](nginx) | Legacy NGINX gateway configuration |
| [outbox-relay](outbox-relay) | Node-based outbox relay helper |
| [prometheus](prometheus) | Prometheus scrape configuration and dashboards |
| [promtail](promtail) | Promtail log shipping configuration |
| [loki](loki) | Loki configuration |
| [grafana](grafana) | Grafana dashboards and datasource provisioning |

## Development Startup

Start the infrastructure stack with default services:

```bash
docker compose up --build
```

With optional data store profiles:

```bash
# Kafka, Cassandra, Elasticsearch stack
docker compose --profile first up --build

# Redpanda, Scylla, OpenSearch stack
docker compose --profile follow up --build

# Both optional stacks
docker compose --profile first --profile follow up --build

# Include legacy NGINX gateway
docker compose --profile legacy up --build
```

## Integration with Other Services

The shared `omnibus` Docker network allows other services to communicate:

- **Backend services** (`../core`) expect this infrastructure to be running
- **Frontend and BFF** (`../ui`) use the API gateway on `http://api-gateway:8080`
- Services can reference infrastructure by hostname (e.g., `postgres:5432`, `redis:6379`, `kafka:9092`)

## Monitoring and Debugging

Access observability tools to monitor the infrastructure:

- **Prometheus**: Scrapes metrics from services on the `omnibus` network
- **Loki**: Aggregates logs via Promtail from container logs
- **Grafana**: Unified dashboard for metrics, logs, and service health
  - Default access: `http://localhost:3001`
  - Datasources: Prometheus (metrics) and Loki (logs)

## Network Configuration

All services use the shared Docker network named `omnibus`. This enables:

- Service-to-service communication by hostname
- Simplified DNS resolution within containers
- Isolated network from the host machine (except exposed ports)
