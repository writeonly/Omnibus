rootProject.name = "omnibus"

include(
    "bidding-engine",
    "event-archive",
    "workflow-orchestrator",
)

project(":bidding-engine").projectDir = file("services/bidding-engine")
project(":event-archive").projectDir = file("services/event-archive")
project(":workflow-orchestrator").projectDir = file("services/workflow-orchestrator")

