package pl.writeonly.omnibus.workflow.domain

data class RulePublicationSubmission(
    val processInstanceKey: String,
    val bpmnProcessId: String,
    val status: String,
    val ruleName: String,
    val requestedBy: String,
)

