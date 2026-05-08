import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management") 
    id("com.google.protobuf") 

    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.omnibus"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

val springCloudVersion = "2025.0.0"
val grpcVersion = "1.66.0"
val protobufVersion = "3.25.5"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
    }
}

dependencies {
    // ======================
    // SPRING BOOT STACK
    // ======================
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("net.devh:grpc-server-spring-boot-starter:3.1.0.RELEASE")
    implementation("net.devh:grpc-client-spring-boot-starter:3.1.0.RELEASE")

    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")

    // ======================
    // CAMUNDA 8 (ZEEBE)
    // ======================
    implementation("io.camunda:camunda-spring-boot-starter:8.8.0")

    // ======================
    // OBSERVABILITY
    // ======================
    implementation("io.micrometer:micrometer-registry-prometheus")

    // ======================
    // OPENAPI
    // ======================
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.6")

    // ======================
    // KOTLIN
    // ======================
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // ======================
    // TESTS
    // ======================
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }

    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
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