plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("jvm")
    kotlin("plugin.spring")

    // 🔥 REQUIRED for proto generation
    alias(libs.plugins.protobuf)
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {

    // ---------------- Kotlin ----------------
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)

    // ---------------- Spring ----------------
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.spring.boot.starter.webflux)

    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.gateway.server.webflux)
    implementation(libs.spring.cloud.starter.loadbalancer)
    implementation(libs.spring.cloud.starter.netflix.eureka.client)

    // ---------------- Observability ----------------
    implementation(libs.logback)
    implementation(libs.micrometer.registry.prometheus)

    // =====================================================
    // 🔥 gRPC CLIENT (API GATEWAY CORE)
    // =====================================================

    implementation(libs.grpc.netty.shaded)
    implementation(libs.grpc.stub)
    implementation(libs.grpc.protobuf)

    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)

    implementation(libs.grpc.spring.client)

    implementation(libs.grpc.kotlin.stub)

    implementation(libs.javax.annotation.api)

    implementation(project(":user-proto"))
    implementation(project(":rule-proto"))
    implementation(project(":workflow-proto"))

    // ---------------- test (opcjonalnie ale warto) ----------------
    testImplementation(libs.spring.boot.starter.test)
}
