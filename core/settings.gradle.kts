rootProject.name = "omnibus"

include(
//    "swing-client",
    "config-server",
    "eureka-server",
    "auth-server",
    "api-gateway",
    "audit-service",
    "user-service",
    "rule-service",
    "workflow-service",
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
project(":rule-service").projectDir = file("service/rule-service")
project(":workflow-service").projectDir = file("service/workflow-service")
project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
