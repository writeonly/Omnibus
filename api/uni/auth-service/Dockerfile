FROM eclipse-temurin:21-jre

WORKDIR /app

COPY server/auth-service/build/libs/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
