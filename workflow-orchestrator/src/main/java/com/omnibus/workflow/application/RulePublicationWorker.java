package com.omnibus.workflow.application;

import io.camunda.client.api.response.ActivatedJob;
import io.camunda.client.annotation.JobWorker;
import java.time.Duration;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RulePublicationWorker {

    private final WebClient webClient;

    public RulePublicationWorker(WebClient.Builder webClientBuilder, WorkflowProperties workflowProperties) {
        this.webClient = webClientBuilder
            .baseUrl(workflowProperties.biddingEngineBaseUrl())
            .build();
    }

    @JobWorker(type = "validate-and-publish-rule")
    public void validateAndPublishRule(ActivatedJob job) {
        Map<String, Object> variables = job.getVariablesAsMap();
        String ruleName = requiredString(variables, "ruleName");
        String ruleContent = requiredString(variables, "ruleContent");

        webClient.post()
            .uri("/api/v1/admin/rules")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of(
                "name", ruleName,
                "content", ruleContent
            ))
            .retrieve()
            .toBodilessEntity()
            .timeout(Duration.ofSeconds(15))
            .block();
    }

    private String requiredString(Map<String, Object> variables, String key) {
        Object value = variables.get(key);
        if (value instanceof String text && !text.isBlank()) {
            return text;
        }

        throw new IllegalArgumentException("Missing workflow variable: " + key);
    }
}
