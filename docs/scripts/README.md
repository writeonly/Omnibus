# Omnibus Scripts

Convenient scripts for development, formatting, building, and running the Omnibus project.

## Setup

First-time setup:
```bash
chmod +x scripts/*.sh
./scripts/setup.sh
```

## Available Scripts

### 1. `quality.sh` - Format, Lint, and Build

**Full pipeline** (format + lint + build):
```bash
./scripts/quality.sh
```

**Format and Build** (skip linting):
```bash
./scripts/quality.sh --skip-lint
```

**Only format TypeScript and Kotlin**:
```bash
./scripts/quality.sh --skip-build --skip-lint
```

**Format, build, and run services**:
```bash
./scripts/quality.sh --run
```

**Skip specific steps**:
```bash
./scripts/quality.sh --skip-kotlin-format --skip-ts-lint --run
```

**View all options**:
```bash
./scripts/quality.sh --help
```

### 2. `setup.sh` - Development Environment Setup

**Initial setup** (install dependencies):
```bash
./scripts/setup.sh
```

Checks for:
- Java 21 (Kotlin/Spring Boot)
- Node.js 20+ (TypeScript/NestJS/Angular)
- npm

Installs:
- All npm dependencies (bff-nest, frontend-angular)
- Sets up executable permissions

### 3. `run.sh` - Build and Run All Services

**Build and start all services**:
```bash
./scripts/run.sh
```

Automatically:
1. Cleans and builds Kotlin modules (Spring Boot)
2. Builds TypeScript module (NestJS)
3. Starts Spring Boot service (port 8080)
4. Starts NestJS service (port 3000)
5. Starts Angular frontend (port 4200) if available

**Access running services**:
- Spring Boot Swagger: http://localhost:8080/swagger-ui.html
- NestJS: http://localhost:3000
- Angular Frontend: http://localhost:4200

Press `Ctrl+C` to stop all services gracefully.

## Quick Start (First Time)

```bash
# 1. Setup environment and install dependencies
./scripts/setup.sh

# 2. Format, lint, build, and run
./scripts/quality.sh --run
```

## Common Workflows

### Development

```bash
# Format and lint only (no build)
./scripts/quality.sh --skip-build

# Quick build and run (skip formatting/linting)
./scripts/quality.sh --skip-format --skip-lint --run

# Format specific language
./scripts/quality.sh --skip-kotlin-lint --skip-ts-lint --skip-build
```

### Pre-commit

```bash
# Full quality check before committing
./scripts/quality.sh
```

### Production Build

```bash
# Format, lint, and build (no run)
./scripts/quality.sh
```

### Testing Running Services

```bash
# Build and run with all quality checks
./scripts/quality.sh --run
```

## Manual Commands

If you prefer to run commands manually:

### Kotlin (Gradle)

```bash
# Format Kotlin code
./gradlew spotlessApply

# Lint Kotlin code
./gradlew detekt

# Full quality check
./gradlew codeQuality

# Build
./gradlew clean build

# Run Spring Boot service
cd services/bidding-engine
./gradlew bootRun
```

### TypeScript (npm)

```bash
# Format TypeScript code
cd bff-nest
npm run format

# Lint TypeScript code
npm run lint

# Type checking
npm run typecheck

# Build
npm run build

# Run development server
npm run start:dev
```

## Script Options Reference

### `quality.sh` Options

| Option | Description |
|--------|-------------|
| `--skip-format` | Skip all formatting (Kotlin + TypeScript) |
| `--skip-kotlin-format` | Skip Kotlin formatting only |
| `--skip-ts-format` | Skip TypeScript formatting only |
| `--skip-lint` | Skip all linting (Kotlin + TypeScript) |
| `--skip-kotlin-lint` | Skip Kotlin linting only |
| `--skip-ts-lint` | Skip TypeScript linting only |
| `--skip-build` | Skip build step |
| `--run` | Start services after build |
| `--help` | Show help message |

## Troubleshooting

### Java not found
```bash
# Install Java 21
# macOS:
brew install openjdk@21

# Linux (Ubuntu):
sudo apt-get install openjdk-21-jdk

# Windows:
# Download from https://adoptium.net/
```

### Node.js version mismatch
```bash
# Check Node.js version
node -v

# Use nvm to switch versions (recommended)
nvm install 20
nvm use 20
```

### Permission denied on scripts
```bash
# Make scripts executable
chmod +x scripts/*.sh
```

### Gradle wrapper not found
```bash
# Ensure you're in the project root
cd /path/to/Omnibus

# Run setup
./scripts/setup.sh
```

## Exit Codes

| Code | Meaning |
|------|---------|
| `0` | Success |
| `1` | Error (formatting, linting, build, or missing prerequisites) |

Scripts will exit immediately on error (fail-fast behavior).
