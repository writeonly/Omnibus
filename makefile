# =========================
# CONFIG
# =========================

COMPOSE_INFRA = docker compose -f docker-compose.infra.yml
COMPOSE_APP   = docker compose -f docker-compose.app.yml
COMPOSE_OBS   = docker compose -f docker-compose.obs.yml

COMPOSE_ALL   = docker compose \
	-f docker-compose.infra.yml \
	-f docker-compose.app.yml \
	-f docker-compose.obs.yml

# =========================
# INFRA
# =========================

infra-up:
	$(COMPOSE_INFRA) up -d --build

infra-down:
	$(COMPOSE_INFRA) down -v

infra-logs:
	$(COMPOSE_INFRA) logs -f

infra-restart:
	$(COMPOSE_INFRA) down
	$(COMPOSE_INFRA) up -d

# =========================
# OBSERVABILITY
# =========================

obs-up:
	$(COMPOSE_INFRA) -f docker-compose.obs.yml up -d

obs-down:
	docker compose -f docker-compose.obs.yml down

obs-logs:
	docker compose -f docker-compose.obs.yml logs -f

# =========================
# FULL STACK (DOCKER)
# =========================

app-up:
	$(COMPOSE_INFRA) -f docker-compose.app.yml up -d

app-down:
	docker compose \
		-f docker-compose.infra.yml \
		-f docker-compose.app.yml down

# =========================
# EVERYTHING
# =========================

all-up:
	$(COMPOSE_ALL) up -d

all-down:
	$(COMPOSE_ALL) down

# =========================
# DEV MODE (TWÓJ GŁÓWNY CASE)
# =========================

dev: infra-up
	@echo "Infra is up. Run your services locally 🚀"

# =========================
# CLEAN
# =========================

clean:
	docker compose down -v --remove-orphans
