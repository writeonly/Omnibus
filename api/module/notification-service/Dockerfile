FROM eclipse-temurin:21-jre

WORKDIR /app

COPY service/notification-service/build/libs/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
