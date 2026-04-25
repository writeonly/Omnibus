plugins {
    // keep root as an aggregator; per-module applies what it needs
}

allprojects {
    group = "com.omnibus"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

tasks.register("format") {
    dependsOn(":services:bidding-engine:spotlessApply")
    dependsOn(":services:event-archive:spotlessApply")
    dependsOn(":services:workflow-orchestrator:spotlessApply")
    description = "Apply Spotless code formatting to all Kotlin modules"
}

tasks.register("formatCheck") {
    dependsOn(":services:bidding-engine:spotlessCheck")
    dependsOn(":services:event-archive:spotlessCheck")
    dependsOn(":services:workflow-orchestrator:spotlessCheck")
    description = "Check code formatting with Spotless (no modifications)"
}

tasks.register("lint") {
    dependsOn(":services:bidding-engine:detekt")
    dependsOn(":services:event-archive:detekt")
    dependsOn(":services:workflow-orchestrator:detekt")
    description = "Run Detekt linter on all Kotlin modules"
}

tasks.register("lintFix") {
    dependsOn(":services:bidding-engine:detektAutoCorrect")
    dependsOn(":services:event-archive:detektAutoCorrect")
    dependsOn(":services:workflow-orchestrator:detektAutoCorrect")
    description = "Run Detekt linter with auto-corrections"
}

tasks.register("codeQuality") {
    dependsOn("format")
    dependsOn("lint")
    description = "Run all code quality checks (format + lint)"
}