plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}"
        )
    }
}

dependencies {

    // Kotlin
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)

    // Spring Boot
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webflux)

    // Messaging (KLUCZOWE dla Notification Service)
    implementation("org.springframework.kafka:spring-kafka")

    // Spring Cloud (event-driven + config)
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.function.web)
    implementation(libs.spring.cloud.function.context)

    // Observability (OpenShift / cloud-native must-have)
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation(libs.micrometer.registry.prometheus)

    // Logging
    implementation(libs.logback)

    // Optional: resilience (retry / circuit breaker)
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    // Tests
    testImplementation(libs.spring.boot.starter.test)
    testImplementation("org.springframework.kafka:spring-kafka-test")
}
