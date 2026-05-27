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
        // Spring Cloud BOM
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")

        // Spring Modulith BOM
        mavenBom("org.springframework.modulith:spring-modulith-bom:${libs.versions.springModulith.get()}")
    }
}

dependencies {

    // ---------------- Spring Boot ----------------
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.data.jpa)

    // jeśli reactive:
    // implementation(libs.spring.boot.starter.webflux)

    // ---------------- Modulith ----------------
    implementation(libs.spring.modulith.core)
    implementation(libs.spring.modulith.events.api)
    runtimeOnly(libs.spring.modulith.events.jpa)

    testImplementation(libs.spring.modulith.test)

    // ---------------- Database ----------------
    runtimeOnly(libs.postgresql)
    implementation(libs.spring.boot.starter.jdbc)
    implementation("org.flywaydb:flyway-core")

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
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.security.test)

    testImplementation(libs.spring.kafka.test)
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
