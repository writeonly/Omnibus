package pl.writeonly.omnibus.workflow.application

import io.camunda.client.api.response.ActivatedJob
import io.camunda.client.annotation.JobWorker
import org.springframework.stereotype.Component
import pl.writeonly.omnibus.workflow.grpc.BiddingEngineGrpcClient

@Component
class RulePublicationWorker(
    private val biddingEngineGrpcClient: BiddingEngineGrpcClient,
) {
    @JobWorker(type = "validate-and-publish-rule")
    fun validateAndPublishRule(job: ActivatedJob) {
        val variables = job.variablesAsMap
        val ruleName = requiredString(variables, "ruleName")
        val ruleContent = requiredString(variables, "ruleContent")

        biddingEngineGrpcClient.saveManagedRule(ruleName, ruleContent)
    }

    private fun requiredString(variables: Map<String, Any>, key: String): String {
        val value = variables[key]
        if (value is String && value.isNotBlank()) return value
        throw IllegalArgumentException("Missing workflow variable: $key")
    }
}
