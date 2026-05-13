plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {

    // Gateway
    implementation(libs.spring.cloud.starter.gateway.server.webflux)

    // Security
    implementation(libs.spring.boot.starter.oauth2.resource.server)

    // Actuator
    implementation(libs.spring.boot.starter.actuator)

    // Observability
    implementation(libs.micrometer.registry.prometheus)
}