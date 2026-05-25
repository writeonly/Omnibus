rootProject.name = "omnibus"

include(
    "user-proto",
    "rule-proto",
    "workflow-proto",

    "audit-service",
    "user-service",
    "rule-service",
    "workflow-service",

    "config-server",
    "eureka-server",
    "auth-server",
    "api-gateway",

    //    "swing-client",
    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

project(":user-proto").projectDir = file("proto/user-proto")
project(":rule-proto").projectDir = file("proto/rule-proto")
project(":workflow-proto").projectDir = file("proto/workflow-proto")

project(":audit-service").projectDir = file("service/audit-service")
project(":user-service").projectDir = file("service/user-service")
project(":rule-service").projectDir = file("service/rule-service")
project(":workflow-service").projectDir = file("service/workflow-service")

project(":config-server").projectDir = file("server/config-server")
project(":eureka-server").projectDir = file("server/eureka-server")
project(":api-gateway").projectDir = file("server/api-gateway")
project(":auth-server").projectDir = file("server/auth-server")

//project(":swing-client").projectDir = file("legacy/swing-client")
project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
