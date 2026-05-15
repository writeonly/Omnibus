FROM eclipse-temurin:21-jre

WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring

USER spring:spring

COPY core/eureka-server/build/libs/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
