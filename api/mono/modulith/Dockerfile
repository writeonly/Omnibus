FROM eclipse-temurin:21-jre

WORKDIR /app

COPY server/modulith/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
