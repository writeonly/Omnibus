plugins {
    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.3"

dependencies {
    // Spring Cloud Stream (Kafka binder)
    implementation("org.springframework.cloud:spring-cloud-starter-stream-kafka")

    // Cassandra
    implementation("org.springframework.boot:spring-boot-starter-data-cassandra")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Actuator (bank-grade observability)
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}
