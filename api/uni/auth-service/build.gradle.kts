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
    implementation(libs.spring.boot.starter.data.redis)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)

    // Spring Cloud
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.function.web)
    implementation(libs.spring.cloud.starter.stream.kafka)
    implementation(libs.spring.cloud.starter.stream.rabbit)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    
    // Spring
    implementation(libs.spring.security.oauth2.authorization.server)
    implementation(libs.spring.jcl)

    // Observability
    implementation(libs.logback)
    implementation(libs.micrometer.registry.prometheus)

    // Keycloak
    implementation("org.keycloak:keycloak-admin-client:26.0.9")

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation("org.testcontainers:testcontainers:1.20.4")
    // testImplementation("org.testcontainers:keycloak:1.20.4")
    testImplementation("com.github.dasniko:testcontainers-keycloak:4.2.1")
    testImplementation("org.keycloak:keycloak-admin-client:25.0.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter")
}
