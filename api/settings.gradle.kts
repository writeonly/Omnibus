rootProject.name = "omnibus"

include(
    //    "swing-client",
    "api-gateway",
    "modulith",
    "config-server",
    "eureka-server",

    "auth-service",
    "user-service",
    "notification-service",
    "audit-service",
    "rule-service",
    "workflow-service",

    "user-proto",
    "rule-proto",
    "workflow-proto",

    "event-archive",
    "outbox-relay-service",
    "cassandra-projection-service"

)

//project(":swing-client").projectDir = file("single/swing-client")
project(":config-server").projectDir = file("mono/config-server")
project(":eureka-server").projectDir = file("mono/eureka-server")
project(":api-gateway").projectDir = file("mono/api-gateway")
project(":modulith").projectDir = file("mono/modulith")

project(":auth-service").projectDir = file("uni/auth-service")
project(":user-service").projectDir = file("uni/user-service")
project(":notification-service").projectDir = file("uni/notification-service")
project(":audit-service").projectDir = file("uni/audit-service")
project(":rule-service").projectDir = file("uni/rule-service")
project(":workflow-service").projectDir = file("uni/workflow-service")

project(":user-proto").projectDir = file("proto/user-proto")
project(":rule-proto").projectDir = file("proto/rule-proto")
project(":workflow-proto").projectDir = file("proto/workflow-proto")

project(":event-archive").projectDir = file("legacy/event-archive")
project(":outbox-relay-service").projectDir = file("legacy/outbox-relay-service")
project(":cassandra-projection-service").projectDir = file("legacy/cassandra-projection-service")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
