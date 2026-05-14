package pl.writeonly.omnibus.workflow.application

import org.springframework.stereotype.Component
import pl.writeonly.omnibus.workflow.grpc.BiddingEngineGrpcClient

@Component
class RulePublicationWorker(
    private val biddingEngineGrpcClient: BiddingEngineGrpcClient,
) {
    fun validateAndPublishRule(variables: Map<String, Any>) {
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
