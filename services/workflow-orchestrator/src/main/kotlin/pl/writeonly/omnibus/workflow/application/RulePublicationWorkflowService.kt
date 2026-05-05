package pl.writeonly.omnibus.workflow.application

import io.camunda.client.CamundaClient
import io.camunda.client.api.response.ProcessInstanceEvent
import org.springframework.stereotype.Service
import pl.writeonly.omnibus.workflow.domain.RulePublicationRequest
import pl.writeonly.omnibus.workflow.domain.RulePublicationSubmission
import java.time.Instant

@Service
class RulePublicationWorkflowService(
    private val camundaClient: CamundaClient,
) {
    fun startPublication(request: RulePublicationRequest): RulePublicationSubmission {
        val event: ProcessInstanceEvent =
            camundaClient.newCreateInstanceCommand()
                .bpmnProcessId(PROCESS_ID)
                .latestVersion()
                .variables(buildVariables(request))
                .send()
                .join()

        return RulePublicationSubmission(
            event.processInstanceKey.toString(),
            PROCESS_ID,
            "STARTED",
            request.name,
            request.requestedBy,
        )
    }

    private fun buildVariables(request: RulePublicationRequest): Map<String, Any> =
        hashMapOf(
            "ruleName" to request.name,
            "ruleContent" to request.content,
            "requestedBy" to request.requestedBy,
            "requestedAt" to Instant.now().toString(),
        )

    companion object {
        const val PROCESS_ID: String = "rule-publication-process"
    }
}
