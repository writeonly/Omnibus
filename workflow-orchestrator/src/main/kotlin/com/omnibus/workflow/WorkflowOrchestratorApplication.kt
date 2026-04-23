package com.omnibus.workflow

import com.omnibus.workflow.application.WorkflowProperties
import io.camunda.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@Deployment(resources = ["classpath*:/bpmn/*.bpmn"])
@EnableConfigurationProperties(WorkflowProperties::class)
class WorkflowOrchestratorApplication

fun main(args: Array<String>) {
    runApplication<WorkflowOrchestratorApplication>(*args)
}

