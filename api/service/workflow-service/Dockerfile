FROM eclipse-temurin:21-jre

WORKDIR /app

COPY service/workflow-service/build/libs/workflow-service-*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
