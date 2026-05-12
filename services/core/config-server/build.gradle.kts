plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}")
    }
}

dependencies {

    // Spring Boot
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)

    // Spring Cloud Config Server
    implementation(libs.spring.cloud.config.server)

    // Kotlin
    implementation(kotlin("stdlib"))

    // Tests
    testImplementation(libs.spring.boot.starter.test)
}
