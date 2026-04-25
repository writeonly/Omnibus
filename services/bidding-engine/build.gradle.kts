import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("com.diffplug.spotless") version "6.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
    implementation("org.springframework.kafka:spring-kafka")

    val droolsVersion = "8.44.0.Final"
    implementation("org.drools:drools-engine:$droolsVersion")
    implementation("org.drools:drools-decisiontables:$droolsVersion")
    implementation("org.drools:drools-mvel:$droolsVersion")
    implementation("org.kie:kie-api:$droolsVersion")
    implementation("org.kie:kie-internal:$droolsVersion")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint("1.0.1")
            .setUseExperimental(true)
        licenseHeaderFile(rootProject.file("LICENSE_HEADER"))
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.0.1")
            .setUseExperimental(true)
    }
}

detekt {
    config.setFrom("$rootDir/detekt.yml")
    buildUponDefaultConfig = true
    ignoreFailures = false
}

tasks.register("codeQuality") {
    dependsOn("spotlessApply")
    dependsOn("detekt")
    description = "Run all code quality checks (format + lint)"
}