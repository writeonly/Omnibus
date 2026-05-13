FROM eclipse-temurin:21-jre

WORKDIR /app

COPY core/bidding-engine/build/libs/bidding-engine-*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
