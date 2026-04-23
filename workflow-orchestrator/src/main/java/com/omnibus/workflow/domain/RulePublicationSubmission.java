package com.omnibus.workflow.domain;

public record RulePublicationSubmission(
    String processInstanceKey,
    String bpmnProcessId,
    String status,
    String ruleName,
    String requestedBy
) {
}
