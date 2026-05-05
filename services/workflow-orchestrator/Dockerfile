# ---------- BUILD STAGE ----------
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

# =========================
# ROOT GRADLE (MONOREPO)
# =========================
COPY gradle ./gradle
COPY gradlew ./
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle.properties ./
COPY services ./services

# =========================
# WARMUP (tylko dependency graph)
# =========================
RUN gradle :workflow-orchestrator:dependencies --no-daemon

# =========================
# BUILD MODULE ONLY
# =========================
RUN gradle :workflow-orchestrator:bootJar --no-daemon


# ---------- RUNTIME ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/services/workflow-orchestrator/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
