import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.api.tasks.testing.Test
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.noarg) apply false

    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.deps) apply false

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

val kotestVersion = libs.versions.kotest.get()
val junitPlatformVersion = libs.versions.junitPlatform.get()

subprojects {

    // ---------------- CORE PLUGINS ----------------
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "com.diffplug.spotless")

    // ---------------- JAVA TOOLCHAIN ----------------
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    // ---------------- KOTLIN ----------------
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    // ---------------- TESTS ----------------
    dependencies {
        "testImplementation"("io.kotest:kotest-runner-junit5:$kotestVersion")
        "testImplementation"("io.kotest:kotest-assertions-core:$kotestVersion")
        "testImplementation"("io.kotest:kotest-extensions-spring:$kotestVersion")
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher:$junitPlatformVersion")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    // ---------------- SPOTLESS ----------------
    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> { }

    tasks.withType<com.diffplug.gradle.spotless.SpotlessTask>().configureEach {
        enabled = false
    }

    // ---------------- BOOT RUN ----------------
    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun>().configureEach {
        jvmArgs("-Xms256m", "-Xmx256m")
    }
}

repositories {
    mavenCentral()
}

// ---------------- SAFE PROCESS RUNNER ----------------

val processes = java.util.concurrent.CopyOnWriteArrayList<Process>()

fun runAsync(name: String, command: String) {
    val process = ProcessBuilder(command.split(" "))
        .inheritIO()
        .start()

    processes.add(process)

    println("🚀 started $name")
}

tasks.register("run") {

    doLast {
        println("🔥 starting full dev environment...")

        runAsync("config-server", "./gradlew :config-server:bootRun")
        runAsync("eureka-server", "./gradlew :eureka-server:bootRun")
        runAsync("api-gateway", "./gradlew :api-gateway:bootRun")
        runAsync("modulith", "./gradlew :modulith:bootRun")
        runAsync("auth-service", "./gradlew :auth-service:bootRun")
        runAsync("audit-service", "./gradlew :audit-service:bootRun")
        runAsync("user-service", "./gradlew :user-service:bootRun")
        runAsync("rule-service", "./gradlew :rule-service:bootRun")
        runAsync("workflow-service", "./gradlew :workflow-service:bootRun")

        Runtime.getRuntime().addShutdownHook(Thread {
            println("🛑 stopping all services...")
            processes.forEach { it.destroyForcibly() }
        })

        processes.forEach { it.waitFor() }
    }
}
