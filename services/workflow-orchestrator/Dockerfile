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

RUN chmod +x gradlew

# =========================
# WARMUP (tylko dependency graph)
# =========================
RUN ./gradlew :services:bidding-engine:dependencies --no-daemon

# =========================
# SOURCE (minimal required)
# =========================
COPY services/bidding-engine/src ./services/bidding-engine/src
COPY services/bidding-engine/build.gradle.kts ./services/bidding-engine/
COPY services/bidding-engine/settings.gradle.kts ./services/bidding-engine/

# =========================
# BUILD MODULE ONLY
# =========================
RUN ./gradlew :services:bidding-engine:bootJar --no-daemon


# ---------- RUNTIME ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/services/bidding-engine/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]