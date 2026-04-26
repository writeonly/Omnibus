rootProject.name = "omnibus"

include(
    "bidding-engine",
    "workflow-orchestrator",
    "event-archive"
)

project(":bidding-engine").projectDir = file("services/bidding-engine")
project(":workflow-orchestrator").projectDir = file("services/workflow-orchestrator")
project(":event-archive").projectDir = file("services/event-archive")
