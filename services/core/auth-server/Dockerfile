FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/auth-server/build/libs/*.jar app.jar

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
