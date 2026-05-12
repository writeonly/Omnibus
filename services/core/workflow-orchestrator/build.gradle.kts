import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    alias(libs.plugins.protobuf)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
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
    }
}

dependencies {

    // ---------------- Spring ----------------
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.cloud.starter.function.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.jdbc)

    // ---------------- gRPC ----------------
    implementation(libs.grpc.spring.server)
    implementation(libs.grpc.spring.client)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.protobuf.java.util)
    compileOnly(libs.javax.annotation.api)

    // ---------------- Kafka ----------------
    implementation(libs.spring.kafka)

    // ---------------- Observability ----------------
    implementation(libs.micrometer.registry.prometheus)

    // ---------------- OpenAPI ----------------
    implementation(libs.springdoc.openapi)

    // ---------------- Kotlin ----------------
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)

    // ---------------- CAMUNDA 8 (ZEEBE) ----------------
    implementation("io.camunda:camunda-spring-boot-starter:8.8.0")

    // ---------------- Test ----------------
    testImplementation(libs.spring.boot.starter.test)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.get()}"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.get()}"
        }
    }

    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}
