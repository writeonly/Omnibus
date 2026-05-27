plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
        mavenBom("org.springframework.modulith:spring-modulith-bom:${libs.versions.springModulith.get()}")
        mavenBom("org.testcontainers:testcontainers-bom:${libs.versions.testcontainers.get()}")
    }
}

dependencies {

    // ---------------- Spring Boot ----------------
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.jdbc)

    // ---------------- Modulith ----------------
    implementation(libs.spring.modulith.core)
    implementation(libs.spring.modulith.events.api)
    runtimeOnly(libs.spring.modulith.events.jpa)
    testImplementation(libs.spring.modulith.test)

    // ---------------- Database ----------------
    runtimeOnly(libs.postgresql)
    implementation(libs.flyway.core)

    // ---------------- JSON / Kotlin ----------------
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)

    // ---------------- Logging ----------------
    implementation(libs.kotlin.logging)

    // ---------------- Lombok ----------------
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)

    // ---------------- Testing ----------------
    testImplementation(libs.junit.jupiter)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)
    testImplementation(libs.spring.kafka.test)

    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)


    testRuntimeOnly(libs.junit.platform.launcher)
}
