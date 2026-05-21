import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    alias(libs.plugins.protobuf)

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {
    // Kotlin
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)

    // Spring
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.cloud.starter.bus.kafka)
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.loadbalancer)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)
    implementation(libs.spring.cloud.starter.function.web)
 
     // Kafka
    implementation(libs.spring.kafka)

    // gRPC
    implementation(libs.grpc.spring.server)
    implementation(libs.grpc.spring.client)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)
    implementation(libs.protobuf.java.util)
    compileOnly(libs.javax.annotation.api)

    // Observability
    implementation(libs.micrometer.registry.prometheus)

    // OpenAPI
    implementation(libs.springdoc.openapi)

    // Kogito
    implementation(libs.jbpm.spring.boot.starter)
    implementation(libs.kogito.api)
    implementation(libs.kogito.dmn)

    // Test
    testImplementation(libs.spring.boot.starter.test)
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
