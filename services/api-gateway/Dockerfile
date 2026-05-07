FROM gradle:8.10-jdk21 AS build
WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradle.properties* ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon || true

COPY src ./src
RUN gradle bootJar --no-daemon


FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
