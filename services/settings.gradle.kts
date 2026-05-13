rootProject.name = "omnibus"

include(
    "config-server",
    "api-gateway",
    "rule-engine",
    "workflow-engine",
    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

project(":config-server").projectDir = file("core/config-server")
project(":api-gateway").projectDir = file("core/api-gateway")
project(":rule-engine").projectDir = file("core/rule-engine")
project(":workflow-engine").projectDir = file("core/workflow-engine")
project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
