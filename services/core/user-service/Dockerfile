FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/user-service/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
