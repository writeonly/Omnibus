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
# CACHE DEPENDENCIES
# =========================
RUN ./gradlew :services:event-archive:dependencies --no-daemon

# =========================
# MODULE SOURCE
# =========================
COPY services/event-archive/src ./services/event-archive/src
COPY services/event-archive/build.gradle.kts ./services/event-archive/
COPY services/event-archive/settings.gradle.kts ./services/event-archive/

# =========================
# BUILD ONLY MODULE
# =========================
RUN ./gradlew :services:event-archive:bootJar --no-daemon


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/services/event-archive/build/libs/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
