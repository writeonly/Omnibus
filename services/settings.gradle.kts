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

project(":config-server").projectDir = file("config-server")
project(":api-gateway").projectDir = file("api-gateway")
project(":bidding-engine").projectDir = file("bidding-engine")
project(":workflow-orchestrator").projectDir = file("workflow-orchestrator")
project(":event-archive").projectDir = file("event-archive")
project(":outbox-relay-service").projectDir = file("outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
