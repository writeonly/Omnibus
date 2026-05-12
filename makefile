# =========================
# CONFIG
# =========================

COMPOSE_INFRA = docker compose -f infra/docker-compose.yml
COMPOSE_SERVICES = docker compose -f services/docker-compose.yml
COMPOSE_PRESENTATION = docker compose -f presentation/docker-compose.yml
COMPOSE_OBS = docker compose -f obs/docker-compose.yml

COMPOSE_ALL = docker compose \
	-f infra/docker-compose.yml \
	-f services/docker-compose.yml \
	-f presentation/docker-compose.yml \
	-f obs/docker-compose.yml

SERVICES_DIR = services
BFF_NEST_DIR = presentation/bff-nest

GRADLE = cd $(SERVICES_DIR) && ./gradlew

# =========================
# FULL BUILD
# =========================

all:
	$(MAKE) services-build
	$(MAKE) infra-up
	$(MAKE) services-up
	$(MAKE) presentation-up

build-all:
	$(MAKE) services-build
	$(MAKE) bff-nest-build
	$(MAKE) frontend-angular-build
#	$(MAKE) frontend-react-build
#	$(MAKE) dev-dashboard-build
	@echo "🔥 FULL BUILD DONE"

build-all-parallel:
	$(MAKE) -j 5 \
		services-build \
		bff-nest-build \
		frontend-angular-build \
		frontend-react-build \
		dev-dashboard-build
	@echo "🔥 FULL PARALLEL BUILD DONE"

# =========================
# DEV MODE
# =========================

dev: infra-up obs-up
	@echo "🚀 Infra + Observability running"

dev-all: build-all infra-up services-up presentation-up obs-up
	@echo "🚀 FULL SYSTEM READY"

# =========================
# INFRA
# =========================

infra-up:
	$(COMPOSE_INFRA) up -d

infra-down:
	$(COMPOSE_INFRA) down -v

infra-logs:
	$(COMPOSE_INFRA) logs -f

infra-restart:
	$(COMPOSE_INFRA) restart

infra-ps:
	$(COMPOSE_INFRA) ps

# =========================
# SERVICES
# =========================

# ---------------------------------
# BUILD SERVICES LOCALLY (NO DOCKER)
# ---------------------------------

services-build:
	$(GRADLE) clean build --parallel --build-cache --no-daemon

services-bootjar:
	$(GRADLE) \
		:config-server:bootJar \
		:api-gateway:bootJar \
		:bidding-engine:bootJar \
		:workflow-orchestrator:bootJar \
		--parallel \
		--build-cache \
		--no-daemon

services-clean:
	$(GRADLE) clean

services-test:
	$(GRADLE) test --parallel --no-daemon

services-coverage:
	$(GRADLE) jacocoTestReport

services-coverage-html:
	$(GRADLE) jacocoTestReport
	open services/build/reports/jacoco/test/html/index.html

# ---------------------------------
# DOCKER SERVICES
# ---------------------------------

services-up: services-bootjar
	$(COMPOSE_SERVICES) up -d --build

services-down:
	$(COMPOSE_SERVICES) down -v

services-logs:
	$(COMPOSE_SERVICES) logs -f

services-restart: services-bootjar
	$(COMPOSE_SERVICES) restart

services-rebuild: services-bootjar
	$(COMPOSE_SERVICES) build --no-cache
	$(COMPOSE_SERVICES) up -d

services-ps:
	$(COMPOSE_SERVICES) ps

# =========================
# PRESENTATION
# =========================

presentation-up:
	$(COMPOSE_PRESENTATION) up -d --build

presentation-down:
	$(COMPOSE_PRESENTATION) down -v

presentation-logs:
	$(COMPOSE_PRESENTATION) logs -f

presentation-restart:
	$(COMPOSE_PRESENTATION) restart

# ---------------------------------
# NEST BFF
# ---------------------------------

bff-nest-build:
	cd $(BFF_NEST_DIR) && npm install && npm run build

bff-nest-dev:
	cd $(BFF_NEST_DIR) && npm run start:dev

# ---------------------------------
# FRONTENDS
# ---------------------------------

frontend-angular-build:
	cd presentation/frontend-angular && npm install && npm run build

frontend-react-build:
	cd presentation/frontend-react && npm install && npm run build

dev-dashboard-build:
	cd presentation/dev-dashboard && npm install && npm run build

# =========================
# OBSERVABILITY
# =========================

obs-up:
	$(COMPOSE_OBS) up -d

obs-down:
	$(COMPOSE_OBS) down -v

obs-logs:
	$(COMPOSE_OBS) logs -f

obs-restart:
	$(COMPOSE_OBS) restart

# =========================
# GLOBAL
# =========================

up: infra-up services-up presentation-up obs-up

down:
	$(COMPOSE_ALL) down -v --remove-orphans

restart:
	$(COMPOSE_ALL) restart

logs:
	$(COMPOSE_ALL) logs -f

ps:
	$(COMPOSE_ALL) ps

# =========================
# CLEAN
# =========================

clean:
	docker compose down -v --remove-orphans
	$(GRADLE) clean

docker-prune:
	docker system prune -af --volumes
