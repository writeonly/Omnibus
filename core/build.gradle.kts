import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.concurrent.Executors

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

    // CORE PLUGINS
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.spring.dependency-management")
    // apply(plugin = "io.gitlab.arturbosch.detekt")
    apply(plugin = "com.diffplug.spotless")

    // JAVA TOOLCHAIN (Java 21 ONLY)
    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    // KOTLIN CONFIG (FULL CONSISTENCY)
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    // TEST CONFIG
    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }

    // DETEKT (GLOBAL SAFE MODE)
    // extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
    //     config.setFrom("$rootDir/detekt.yml")
    //     buildUponDefaultConfig = true
    //     ignoreFailures = true
    // }

    // SPOTLESS (GLOBAL)
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

    tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
        jvmArgs = listOf(
            "-Xms256m",
            "-Xmx256m"
        )
    }
}

repositories {
    mavenCentral()
}

val processes = mutableListOf<Process>()

fun runAsync(name: String, command: String) {
    val pb = ProcessBuilder(command.split(" "))
        .inheritIO()

    val process = pb.start()
    processes.add(process)

    println("🚀 started $name")
}

tasks.register("run") {

    doLast {
        println("🔥 starting full dev environment...")

        runAsync("config-server", "./gradlew :config-server:bootRun")
        runAsync("eureka-server", "./gradlew :eureka-server:bootRun")
        runAsync("auth-server", "./gradlew :auth-server:bootRun")
        runAsync("api-gateway", "./gradlew :api-gateway:bootRun")
        runAsync("audit-service", "./gradlew :audit-service:bootRun")
        runAsync("user-service", "./gradlew :user-service:bootRun")
        runAsync("rule-service", "./gradlew :rule-service:bootRun")
        runAsync("workflow-service", "./gradlew :workflow-service:bootRun")

        Runtime.getRuntime().addShutdownHook(Thread {
            println("🛑 stopping all services...")
            processes.forEach { it.destroy() }
        })

        processes.forEach { it.waitFor() }
    }
}