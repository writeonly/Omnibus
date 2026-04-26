# ---------- BUILD STAGE ----------
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./

RUN chmod +x gradlew

# cache dependencies first (important)
RUN ./gradlew dependencies --no-daemon

COPY src ./src

RUN ./gradlew bootJar --no-daemon


# ---------- RUNTIME STAGE ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
