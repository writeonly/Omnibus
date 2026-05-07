# ---- BUILD STAGE ----
FROM gradle:8.7-jdk17 AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY src src

RUN gradle clean bootJar --no-daemon

# ---- RUNTIME STAGE ----
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

# domyślny port Spring Boot (jeśli kiedykolwiek wystawisz HTTP)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
