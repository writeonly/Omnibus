plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
    }
}

dependencies {

    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    implementation(
        "org.springframework.boot:spring-boot-starter-oauth2-resource-server"
    )

    implementation(
        "org.springframework.boot:spring-boot-starter-actuator"
    )

    implementation(
        "io.micrometer:micrometer-registry-prometheus"
    )
}
