FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/workflow-engine/build/libs/workflow-engine-*.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
