FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/audit-service/build/libs/*.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
