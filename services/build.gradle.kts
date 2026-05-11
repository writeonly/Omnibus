import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.noarg) apply false

    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.deps) apply false

    // alias(libs.plugins.detekt) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.protobuf) apply false
}

allprojects {
    group = "com.omnibus"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {

    // =========================
    // CORE PLUGINS
    // =========================
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")
    // apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    // =========================
    // JAVA TOOLCHAIN (Java 21 ONLY)
    // =========================
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    // =========================
    // KOTLIN CONFIG (FULL CONSISTENCY)
    // =========================
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    // =========================
    // TEST CONFIG
    // =========================
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    // =========================
    // DETEKT (GLOBAL SAFE MODE)
    // =========================
    // extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    //     config.setFrom("$rootDir/detekt.yml")
    //     buildUponDefaultConfig = true
    //     ignoreFailures = true
    // }

    // =========================
    // SPOTLESS (GLOBAL)
    // =========================
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        // kotlin {
        //     target("src/**/*.kt")
        //     // ktlint("1.3.0")
        //     ktfmt().kotlinlangStyle()
        //     // prettier(...)
        //     trimTrailingWhitespace()
        //     endWithNewline()
        //     // target("DO_NOT_MATCH_ANYTHING")
        // }

        // kotlinGradle {
        //     target("*.gradle.kts")
        //     // ktlint("1.3.0")
        // }
    }
    tasks.withType<com.diffplug.gradle.spotless.SpotlessTask>().configureEach {
        enabled = false
    }
}

repositories {
    mavenCentral()
}