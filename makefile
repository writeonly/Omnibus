# =========================
# CONFIG
# =========================

PROFILE = "follow"

COMPOSE_INFRA        = docker compose --profile $(PROFILE) -f infra/docker-compose.yml
COMPOSE_CORE     = docker compose --profile $(PROFILE) -f core/docker-compose.yml
COMPOSE_PRESENTATION = docker compose --profile $(PROFILE) -f presentation/docker-compose.yml
COMPOSE_OBS          = docker compose --profile $(PROFILE) -f obs/docker-compose.yml

COMPOSE_ALL = docker compose \
	--profile $(PROFILE) \
	-f infra/docker-compose.yml \
	-f core/docker-compose.yml \
	-f presentation/docker-compose.yml \
	-f obs/docker-compose.yml

CORE_DIR = core
BFF_NEST_DIR = presentation/bff-nest

GRADLE = cd $(CORE_DIR) && ./gradlew

# =========================
# FULL BUILD
# =========================

all:
	$(MAKE) core-build
	$(MAKE) infra-up
	$(MAKE) core-up
	$(MAKE) presentation-up

down:
	$(MAKE) presentation-down
	$(MAKE) infra-down
	$(MAKE) core-down
	$(MAKE) obs-down

build-all:
	$(MAKE) core-build
	$(MAKE) bff-nest-build
	$(MAKE) frontend-angular-build
#	$(MAKE) frontend-react-build
#	$(MAKE) dev-dashboard-build
	@echo "🔥 FULL BUILD DONE"

build-all-parallel:
	$(MAKE) -j 5 \
		core-build \
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

dev-all: build-all infra-up core-up presentation-up obs-up
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
# CORE
# =========================

# ---------------------------------
# BUILD CORE LOCALLY (NO DOCKER)
# ---------------------------------

core-build:
	$(GRADLE) clean build --parallel --build-cache --no-daemon

core-bootjar:
	$(GRADLE) \
		:config-server:bootJar \
		:api-gateway:bootJar \
		:rule-engine:bootJar \
		:workflow-engine:bootJar \
		--parallel \
		--build-cache \
		--no-daemon

core-clean:
	$(GRADLE) clean

core-test:
	$(GRADLE) test --parallel --no-daemon

core-coverage:
	$(GRADLE) jacocoTestReport

core-coverage-html:
	$(GRADLE) jacocoTestReport
	open core/build/reports/jacoco/test/html/index.html

# ---------------------------------
# DOCKER SERVICES
# ---------------------------------

core-up: core-bootjar
	$(COMPOSE_CORE) up -d --build

core-down:
	$(COMPOSE_CORE) down -v

core-logs:
	$(COMPOSE_CORE) logs -f

core-restart: core-bootjar
	$(COMPOSE_CORE) restart

core-rebuild: core-bootjar
	$(COMPOSE_CORE) build --no-cache
	$(COMPOSE_CORE) up -d

core-ps:
	$(COMPOSE_CORE) ps

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

up: infra-up core-up presentation-up obs-up

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
