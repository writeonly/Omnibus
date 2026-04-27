.PHONY: help up down build backend frontend infra clean logs

help:
	@echo "Available commands:"
	@echo "  make up         - start full system (docker-compose)"
	@echo "  make down       - stop system"
	@echo "  make build      - build all services"
	@echo "  make backend    - build all JVM services"
	@echo "  make frontend   - build Angular"
	@echo "  make infra      - start infra only"
	@echo "  make clean      - clean builds"
	@echo "  make logs       - follow logs"

# =========================
# FULL SYSTEM
# =========================

up:
	docker-compose up --build

down:
	docker-compose down

# =========================
# BUILD ALL
# =========================

build: backend frontend

# =========================
# BACKEND (Gradle JVM services)
# =========================

backend:
	./gradlew clean build

# optionally per-service builds:
bidding-engine:
	./gradlew :services:bidding-engine:build

event-archive:
	./gradlew :services:event-archive:build

workflow-orchestrator:
	./gradlew :services:workflow-orchestrator:build

# =========================
# FRONTEND (Angular)
# =========================

frontend:
	cd presentation/frontend-angular && npm run build

frontend-dev:
	cd presentation/frontend-angular && npm start

frontend-install:
	cd presentation/frontend-angular && npm install

frontend-clean-install:
	cd presentation/frontend-angular && \
	rm -rf node_modules package-lock.json .angular dist && \
	npm cache clean --force && \
	npm install

frontend-fix:
	cd presentation/frontend-angular && \
	rm -rf node_modules .angular && \
	npm install

# =========================
# INFRA ONLY
# =========================

infra:
	docker-compose up prometheus grafana keycloak

# =========================
# CLEAN
# =========================

clean:
	./clean-gradle.sh
	cd presentation/frontend-angular && rm -rf node_modules dist

# =========================
# LOGS
# =========================

logs:
	docker-compose logs -f
