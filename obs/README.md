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
