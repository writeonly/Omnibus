FROM eclipse-temurin:21-jre

WORKDIR /app

COPY workflow-orchestrator/build/libs/workflow-orchestrator-*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]