package pl.writeonly.omnibus.workflow

import io.camunda.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import pl.writeonly.omnibus.workflow.application.WorkflowProperties

@SpringBootApplication
@Deployment(resources = ["classpath*:/bpmn/*.bpmn"])
@EnableConfigurationProperties(WorkflowProperties::class)
class WorkflowOrchestratorApplication

fun main(args: Array<String>) {
    runApplication<WorkflowOrchestratorApplication>(*args)
}

