FROM gradle:8.10-jdk21 AS build

WORKDIR /app

# 1. Gradle wrapper + config
COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts gradle.properties ./

RUN chmod +x gradlew

# 2. cache dependencies
RUN ./gradlew dependencies --no-daemon

# 3. source code (dopiero tutaj!)
COPY src ./src

# 4. build
RUN ./gradlew bootJar --no-daemon


FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
