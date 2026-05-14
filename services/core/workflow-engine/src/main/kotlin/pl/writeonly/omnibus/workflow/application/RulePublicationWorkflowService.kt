package pl.writeonly.omnibus.workflow.application

import org.kie.kogito.process.Processes
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Service
import pl.writeonly.omnibus.workflow.domain.RulePublicationRequest
import pl.writeonly.omnibus.workflow.domain.RulePublicationSubmission
import java.time.Instant
import java.util.UUID

@Service
class RulePublicationWorkflowService(
    private val kogitoProcesses: ObjectProvider<Processes>,
    private val rulePublicationWorker: RulePublicationWorker,
) {
    fun startPublication(request: RulePublicationRequest): RulePublicationSubmission {
        val variables = buildVariables(request)
        val processInstanceId = startKogitoProcess(variables)

        rulePublicationWorker.validateAndPublishRule(variables)

        return RulePublicationSubmission(
            processInstanceId,
            PROCESS_ID,
            "COMPLETED",
            request.name,
            request.requestedBy,
        )
    }

    private fun startKogitoProcess(variables: Map<String, Any>): String {
        val processes = kogitoProcesses.ifAvailable ?: return UUID.randomUUID().toString()
        val process = processes.processById(PROCESS_ID)
        val model = process.createModel()
        model.update(variables)
        val processInstance = process.createInstance(model)

        processInstance.start()

        return processInstance.id()
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
