package pl.writeonly.omnibus.workflow.grpc

import pl.writeonly.omnibus.workflow.domain.RulePublicationRequest
import pl.writeonly.omnibus.workflow.domain.RulePublicationSubmission
import pl.writeonly.omnibus.grpc.workflow.v1.RulePublicationRequest as GrpcRulePublicationRequest
import pl.writeonly.omnibus.grpc.workflow.v1.RulePublicationSubmission as GrpcRulePublicationSubmission

fun GrpcRulePublicationRequest.toDomain(): RulePublicationRequest =
    RulePublicationRequest(
        name = name,
        content = content,
        requestedBy = requestedBy,
    )

fun RulePublicationSubmission.toGrpc(): GrpcRulePublicationSubmission =
    GrpcRulePublicationSubmission.newBuilder()
        .setProcessInstanceKey(processInstanceKey)
        .setBpmnProcessId(bpmnProcessId)
        .setStatus(status)
        .setRuleName(ruleName)
        .setRequestedBy(requestedBy)
        .build()
