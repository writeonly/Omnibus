# ---------- BUILD STAGE ----------
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

# Gradle wrapper + config (CACHE LAYER)
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./

RUN chmod +x gradlew

# cache dependencies first
RUN ./gradlew dependencies --no-daemon

# source code last
COPY src ./src

RUN ./gradlew bootJar --no-daemon


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]