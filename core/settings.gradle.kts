rootProject.name = "omnibus"

include(
//    "swing-client",
    "config-server",
    "eureka-server",
    "auth-server",
    "api-gateway",
    "audit-service",
    "user-service",
    "rule-engine",
    "workflow-engine",
    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

//project(":swing-client").projectDir = file("legacy/swing-client")
project(":config-server").projectDir = file("server/config-server")
project(":eureka-server").projectDir = file("server/eureka-server")
project(":auth-server").projectDir = file("server/auth-server")
project(":api-gateway").projectDir = file("server/api-gateway")
project(":audit-service").projectDir = file("service/audit-service")
project(":user-service").projectDir = file("service/user-service")
project(":rule-engine").projectDir = file("service/rule-engine")
project(":workflow-engine").projectDir = file("service/workflow-engine")
project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
