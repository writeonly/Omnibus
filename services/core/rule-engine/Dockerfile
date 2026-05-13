FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/rule-engine/build/libs/rule-engine-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
