FROM eclipse-temurin:21-jre

WORKDIR /app

COPY service/rule-engine/build/libs/rule-engine-*.jar app.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
