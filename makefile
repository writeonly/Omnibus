# =========================
# CONFIG
# =========================

COMPOSE_INFRA = docker compose -f infra/docker-compose.yml
COMPOSE_SERVICES   = docker compose -f services/docker-compose.yml
COMPOSE_PRESENTATION   = docker compose -f presentation/docker-compose.yml
COMPOSE_OBS   = docker compose -f obs/docker-compose.yml

COMPOSE_ALL   = docker compose \
	-f infra/docker-compose.yml \
	-f services/docker-compose.yml \
	-f presentation/docker-compose.yml \
	-f obs/docker-compose.yml

BFF_NEST_DIR = presentation/bff-nest

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
	$(COMPOSE_INFRA) up -d --build

infra-ps:
	$(COMPOSE_INFRA) ps

# =========================
# SERVICES
# =========================

services-up:
	$(COMPOSE_SERVICES) up -d --build

services-down:
	$(COMPOSE_SERVICES) down -v

services-logs:
	$(COMPOSE_SERVICES) logs -f

services-restart:
	$(COMPOSE_SERVICES) down
	$(COMPOSE_SERVICES) up -d --build

services-build:
	cd services && ./gradlew build

services-clean:
	cd services && ./gradlew clean

services-test:
	cd services && ./gradlew test

services-coverage:
	cd services && ./gradlew jacocoTestReport

services-coverage-html:
	cd services && ./gradlew jacocoTestReport && open build/reports/jacoco/test/html/index.html

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
	$(COMPOSE_PRESENTATION) down
	$(COMPOSE_PRESENTATION) up -d --build

bff-nest-build:
	cd $(BFF_NEST_DIR) && npm install && npm run build

bff-nest-dev:
	cd $(BFF_NEST_DIR) && npm run start:dev

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
	$(COMPOSE_OBS) up -d --build

obs-down:
	$(COMPOSE_OBS) down -v

obs-logs:
	$(COMPOSE_OBS) logs -f

# =========================
# FULL BUILD
# =========================

build-all:
	$(MAKE) services-build
	$(MAKE) bff-nest-build
	$(MAKE) frontend-angular-build
	$(MAKE) frontend-react-build
	$(MAKE) dev-dashboard-build
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
	@echo "Infra + Observability running 🚀"

dev-all: infra-up app-up obs-up build-all
	@echo "FULL SYSTEM READY 🚀"

# =========================
# CLEAN
# =========================

clean:
	docker compose down -v --remove-orphans
	cd services && ./gradlew clean
