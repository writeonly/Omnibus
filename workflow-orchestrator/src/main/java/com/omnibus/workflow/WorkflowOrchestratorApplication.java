package com.omnibus.workflow;

import com.omnibus.workflow.application.WorkflowProperties;
import io.camunda.client.annotation.Deployment;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "classpath*:/bpmn/*.bpmn")
@EnableConfigurationProperties(WorkflowProperties.class)
public class WorkflowOrchestratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowOrchestratorApplication.class, args);
    }
}
