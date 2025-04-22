import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.spring") version "2.0.10"
    kotlin("plugin.jpa") version "2.0.10"

    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.hibernate.orm") version "6.6.8.Final"
    id("org.graalvm.buildtools.native") version "0.10.5"
    id("io.gitlab.arturbosch.detekt") version "1.23.7"
    id("jacoco")
}

group = "pl.writeonly"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

//extra["vaadinVersion"] = "24.3.9"

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.7")
    detektPlugins("pl.setblack:kure-potlin:0.7.0")

    implementation("io.vavr:vavr:0.10.6")

    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")

//    implementation("org.springframework.boot:spring-boot-starter-aot")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("com.vaadin:vaadin-spring-boot-starter")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    implementation("org.zalando:logbook-spring-boot-starter:3.0.0")

    implementation("org.openl:org.openl.core:5.27.10")
    implementation("org.openl.rules:org.openl.rules:5.27.10")

    implementation("org.drools:drools-core:10.0.0")
    implementation("org.kie:kie-spring:7.74.1.Final")

    implementation("com.deliveredtechnologies:rulebook-core:0.12")
    implementation("com.deliveredtechnologies:rulebook-spring:0.12")

    implementation("org.jeasy:easy-rules-core:4.1.0")

    implementation("me.alllex.parsus:parsus-jvm:0.6.1")
    implementation("com.sksamuel.tribune:tribune-core-jvm:1.2.4")

    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("com.h2database:h2")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.kotest:kotest-framework-datatest:5.8.0")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.graphql:spring-graphql-test")
    testImplementation("com.tngtech.archunit:archunit-junit5:1.2.0")
}

dependencyManagement {
    imports {
//        mavenBom("com.vaadin:vaadin-bom:${property("vaadinVersion")}")
    }
}

hibernate {
    enhancement {
        enableAssociationManagement = true
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

springBoot {
    mainClass = "pl.writeonly.omnibus.OmnibusApplicationKt"
}
