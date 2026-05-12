FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/api-gateway/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
