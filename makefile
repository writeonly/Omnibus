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
# PATHS
# =========================

ANGULAR_DIR      = presentation/frontend-angular
REACT_DIR        = presentation/frontend-react
DEV_DASHBOARD_DIR= presentation/dev-dashboard
BFF_NEST_DIR     = presentation/bff-nest

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
	$(COMPOSE_OBS) up -d

obs-down:
	$(COMPOSE_OBS) down

obs-logs:
	$(COMPOSE_OBS) logs -f

# =========================
# DOCKER APP STACK
# =========================

app-up:
	$(COMPOSE_APP) up -d --build

app-down:
	$(COMPOSE_APP) down -v

# =========================
# KOTLIN SERVICES (GRADLE)
# =========================

services-build:
	gradle build

services-clean:
	gradle clean

# =========================
# FRONTENDS
# =========================

frontend-angular-build:
	cd $(ANGULAR_DIR) && npm install && npm run build

frontend-react-build:
	cd $(REACT_DIR) && npm install && npm run build

dev-dashboard-build:
	cd $(DEV_DASHBOARD_DIR) && npm install && npm run build

# =========================
# BFF (NESTJS)
# =========================

bff-nest-build:
	cd $(BFF_NEST_DIR) && npm install && npm run build

# =========================
# FULL BUILD
# =========================

build-all:
	$(MAKE) services-build
	$(MAKE) frontend-angular-build
	$(MAKE) frontend-react-build
	$(MAKE) dev-dashboard-build
	$(MAKE) bff-nest-build
	@echo "🔥 FULL BUILD DONE"

# parallel version (faster on CI / powerful machines)
build-all-parallel:
	$(MAKE) -j 5 services-build frontend-angular-build frontend-react-build dev-dashboard-build bff-nest-build
	@echo "🔥 FULL PARALLEL BUILD DONE"

# =========================
# DEV MODE
# =========================

dev: infra-up
	@echo "Infra is up. Run services locally 🚀"

dev-all: infra-up build-all
	@echo "System ready 🚀"

# =========================
# CLEAN
# =========================

clean:
	docker compose down -v --remove-orphans
	./gradlew clean
