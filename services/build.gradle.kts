import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.25" apply false
    kotlin("plugin.spring") version "1.9.25" apply false

    id("org.springframework.boot") version "3.5.0" apply false
    id("io.spring.dependency-management") version "1.1.6" apply false
}

extra["springCloudVersion"] = "2025.0.0"

allprojects {

    group = "com.omnibus"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")

    extensions.configure<JavaPluginExtension> {

        toolchain {

            languageVersion.set(
                JavaLanguageVersion.of(21)
            )
        }
    }

    tasks.withType<KotlinCompile>().configureEach {

        compilerOptions {

            jvmTarget.set(
                JvmTarget.JVM_21
            )
        }
    }

    tasks.withType<Test>().configureEach {

        useJUnitPlatform()
    }
}
