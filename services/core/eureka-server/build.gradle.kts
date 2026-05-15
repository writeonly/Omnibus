plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.deps)

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencyManagement {
    imports {
        mavenBom(
            "org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.get()}"
        )
    }
}

dependencies {
    // Kotlin
    implementation(libs.jackson.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.logging)

    // Spring
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.cloud.starter.config)
    implementation(libs.spring.cloud.starter.netflix.eureka.server)

    testImplementation(libs.spring.boot.starter.test)
}

springBoot {
    mainClass.set("pl.writeonly.omnibus.eureka.EurekaServerApplication")
}
