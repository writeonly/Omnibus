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
    implementation(libs.spring.boot.starter.data.cassandra)
    implementation(libs.spring.boot.starter.data.elasticsearch)
    implementation(libs.spring.boot.starter.data.mongodb)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webflux)

    // Spring Cloud
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    implementation(libs.spring.cloud.starter.function.web)
    implementation(libs.spring.cloud.starter.stream.kafka)
    implementation(libs.spring.cloud.function.context)

    // Kafka
    implementation(libs.spring.kafka)

    // Observability
    implementation(libs.micrometer.registry.prometheus)

    // Tests
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
}
