# =========================
# CONFIG
# =========================

# PROFILE = "first"
# PROFILE = "follow"
PROFILE = "false"

COMPOSE_INFRA = docker compose --profile $(PROFILE) -f infra/docker-compose.yml
COMPOSE_INIT  = docker compose --profile $(PROFILE) -p init -f infra/docker-compose.init.yml
COMPOSE_API   = docker compose --profile $(PROFILE) -f api/docker-compose.yml
COMPOSE_UI    = docker compose --profile $(PROFILE) -f ui/docker-compose.yml

COMPOSE_ALL = docker compose \
	--profile $(PROFILE) \
	-f infra/docker-compose.yml \
	-f infra/docker-compose.init.yml \
	-f api/docker-compose.yml \
	-f ui/docker-compose.yml

API_DIR = api
BFF_NEST_DIR = presentation/bff-nest

GRADLE = cd $(API_DIR) && ./gradlew

# =========================
# FULL BUILD
# =========================

all:
	$(MAKE) api-build
	$(MAKE) infra-up
	$(MAKE) api-up
	$(MAKE) ui-up

down:
	$(MAKE) ui-down
	$(MAKE) api-down
	$(MAKE) infra-down

build-all:
	$(MAKE) api-build
	$(MAKE) bff-nest-build
	$(MAKE) frontend-angular-build
#	$(MAKE) frontend-react-build
#	$(MAKE) dev-dashboard-build
	@echo "🔥 FULL BUILD DONE"

build-all-parallel:
	$(MAKE) -j 5 \
		api-build \
		bff-nest-build \
		frontend-angular-build \
		frontend-react-build \
		dev-dashboard-build
	@echo "🔥 FULL PARALLEL BUILD DONE"



# =========================
# GLOBAL
# =========================

up: infra-up api-up ui-up

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

dev-all: build-all infra-up api-up ui-up
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
# API
# =========================

# ---------------------------------
# BUILD API LOCALLY (NO DOCKER)
# ---------------------------------

api-build:
	$(GRADLE) clean build --parallel --build-cache --no-daemon

api-bootjar:
	$(GRADLE) \
		:config-server:bootJar \
		:api-gateway:bootJar \
		:rule-service:bootJar \
		:workflow-service:bootJar \
		--parallel \
		--build-cache \
		--no-daemon

api-clean:
	$(GRADLE) clean

api-test:
	$(GRADLE) test --parallel --no-daemon

api-coverage:
	$(GRADLE) jacocoTestReport

api-coverage-html:
	$(GRADLE) jacocoTestReport
	open api/build/reports/jacoco/test/html/index.html

# ---------------------------------
# DOCKER SERVICES
# ---------------------------------

api-up: api-bootjar
	$(COMPOSE_API) up -d --build

api-down:
	$(COMPOSE_API) down -v

api-logs:
	$(COMPOSE_API) logs -f

api-restart: api-bootjar
	$(COMPOSE_API) restart

api-rebuild: api-bootjar
	$(COMPOSE_API) build --no-cache
	$(COMPOSE_API) up -d

api-ps:
	$(COMPOSE_API) ps

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


