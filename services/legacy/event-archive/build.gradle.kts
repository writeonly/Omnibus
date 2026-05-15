plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.noarg")
}

group = "com.omnibus"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}"
        )
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

noArg {
    annotation("org.springframework.data.cassandra.core.mapping.Table")
    annotation("org.springframework.data.cassandra.core.mapping.PrimaryKeyClass")
}

dependencies {

    // Kotlin
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)

    // Spring Boot
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.validation)

    // Cassandra (REACTIVE)
    implementation(libs.spring.boot.starter.data.cassandra)

    // Kafka
    implementation(libs.spring.kafka)

    // Spring Cloud
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    implementation(libs.spring.cloud.starter.function.web)

    // Observability
    implementation(libs.micrometer.registry.prometheus)

    // Tests
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.kafka.test)
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

tasks.register("codeQuality") {
    dependsOn("spotlessApply")
    dependsOn("detekt")
    description = "Run all code quality checks (format + lint)"
}
