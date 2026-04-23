package com.omnibus.workflow.application

import io.camunda.client.api.response.ActivatedJob
import io.camunda.client.annotation.JobWorker
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Component
class RulePublicationWorker(
    webClientBuilder: WebClient.Builder,
    workflowProperties: WorkflowProperties,
) {
    private val webClient: WebClient =
        webClientBuilder
            .baseUrl(workflowProperties.biddingEngineBaseUrl)
            .build()

    @JobWorker(type = "validate-and-publish-rule")
    fun validateAndPublishRule(job: ActivatedJob) {
        val variables = job.variablesAsMap
        val ruleName = requiredString(variables, "ruleName")
        val ruleContent = requiredString(variables, "ruleContent")

        webClient.post()
            .uri("/api/v1/admin/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(mapOf("name" to ruleName, "content" to ruleContent))
            .retrieve()
            .toBodilessEntity()
            .timeout(Duration.ofSeconds(15))
            .block()
    }

    private fun requiredString(variables: Map<String, Any>, key: String): String {
        val value = variables[key]
        if (value is String && value.isNotBlank()) return value
        throw IllegalArgumentException("Missing workflow variable: $key")
    }
}

