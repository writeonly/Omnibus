package com.omnibus.workflow.application;

import com.omnibus.workflow.domain.RulePublicationRequest;
import com.omnibus.workflow.domain.RulePublicationSubmission;
import io.camunda.client.CamundaClient;
import io.camunda.client.api.response.ProcessInstanceEvent;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
public class RulePublicationWorkflowService {

    static final String PROCESS_ID = "rule-publication-process";

    private final CamundaClient camundaClient;

    public RulePublicationWorkflowService(CamundaClient camundaClient) {
        this.camundaClient = camundaClient;
    }

    public Mono<RulePublicationSubmission> startPublication(RulePublicationRequest request) {
        return Mono.fromCallable(() -> {
                ProcessInstanceEvent event = camundaClient.newCreateInstanceCommand()
                    .bpmnProcessId(PROCESS_ID)
                    .latestVersion()
                    .variables(buildVariables(request))
                    .send()
                    .join();

                return new RulePublicationSubmission(
                    Long.toString(event.getProcessInstanceKey()),
                    PROCESS_ID,
                    "STARTED",
                    request.name(),
                    request.requestedBy()
                );
            })
            .subscribeOn(Schedulers.boundedElastic());
    }

    private Map<String, Object> buildVariables(RulePublicationRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("ruleName", request.name());
        variables.put("ruleContent", request.content());
        variables.put("requestedBy", request.requestedBy());
        variables.put("requestedAt", Instant.now().toString());
        return variables;
    }
}
