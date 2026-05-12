rootProject.name = "omnibus"

include(
    "config-server",
    "api-gateway",
    "bidding-engine",
    "workflow-orchestrator",
    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

project(":config-server").projectDir = file("core/config-server")
project(":api-gateway").projectDir = file("core/api-gateway")
project(":bidding-engine").projectDir = file("core/bidding-engine")
project(":workflow-orchestrator").projectDir = file("core/workflow-orchestrator")
project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
