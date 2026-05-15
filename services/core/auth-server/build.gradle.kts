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
    implementation(libs.spring.boot.starter.oauth2.resource.server)

    // Spring Cloud
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.gateway)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)

    // Monitoring
    implementation(libs.micrometer.registry.prometheus)

    // Testing
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
}