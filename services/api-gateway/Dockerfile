FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./
COPY build.gradle.kts ./
COPY settings.gradle.kts ./
COPY gradle.properties ./

COPY services ./services

RUN gradle :api-gateway:dependencies --no-daemon

RUN gradle :api-gateway:bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/services/api-gateway/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
