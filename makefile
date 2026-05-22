# =========================
# CONFIG
# =========================

# PROFILE = "first"
# PROFILE = "follow"
PROFILE = "false"

COMPOSE_INFRA = docker compose --profile $(PROFILE) -f infra/docker-compose.yml
COMPOSE_INIT  = docker compose --profile $(PROFILE) -p init -f infra/docker-compose.init.yml
COMPOSE_CORE  = docker compose --profile $(PROFILE) -f core/docker-compose.yml
COMPOSE_UI    = docker compose --profile $(PROFILE) -f ui/docker-compose.yml

COMPOSE_ALL = docker compose \
	--profile $(PROFILE) \
	-f infra/docker-compose.yml \
	-f infra/docker-compose.init.yml \
	-f core/docker-compose.yml \
	-f ui/docker-compose.yml

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
	$(MAKE) ui-up

down:
	$(MAKE) ui-down
	$(MAKE) core-down
	$(MAKE) infra-down

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
# GLOBAL
# =========================

up: infra-up core-up ui-up

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

# =========================
# DEV MODE
# =========================

dev: infra-up
	@echo "🚀 Infra running"

dev-all: build-all infra-up core-up ui-up
	@echo "🚀 FULL SYSTEM READY"

# =========================
# INFRA
# =========================

infra-up:
	$(COMPOSE_INFRA) up -d
	$(COMPOSE_INIT) up -d

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
		:rule-service:bootJar \
		:workflow-service:bootJar \
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

ui-up:
	$(COMPOSE_UI) up -d --build

ui-down:
	$(COMPOSE_UI) down -v

ui-logs:
	$(COMPOSE_UI) logs -f

ui-restart:
	$(COMPOSE_UI) restart

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
	cd ui/frontend-angular && npm install && npm run build

frontend-react-build:
	cd ui/frontend-react && npm install && npm run build

dev-dashboard-build:
	cd ui/dev-dashboard && npm install && npm run build


