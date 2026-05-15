rootProject.name = "omnibus"

include(
//    "swing-client",
    "config-server",
    "eureka-server",
    "auth-server",
    "api-gateway",
    "user-service",
    "rule-engine",
    "workflow-engine",
    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

//project(":swing-client").projectDir = file("core/swing-client")
project(":config-server").projectDir = file("core/config-server")
project(":eureka-server").projectDir = file("core/eureka-server")
project(":auth-server").projectDir = file("core/auth-server")
project(":api-gateway").projectDir = file("core/api-gateway")
project(":user-service").projectDir = file("core/user-service")
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
