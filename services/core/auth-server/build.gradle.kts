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
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.data.jpa)

    // OAuth2 / Authorization Server
    implementation(libs.spring.security.oauth2.authorization.server)

    // Database
    runtimeOnly(libs.postgresql)

    // Observability
    implementation(libs.micrometer.registry.prometheus)

    // Tests
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
}
