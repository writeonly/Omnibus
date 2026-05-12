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
# CACHE DEPENDENCIES
# =========================
RUN ./gradlew :event-archive:dependencies --no-daemon

# =========================
# BUILD ONLY MODULE
# =========================
RUN ./gradlew :event-archive:bootJar --no-daemon


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/services/event-archive/build/libs/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
