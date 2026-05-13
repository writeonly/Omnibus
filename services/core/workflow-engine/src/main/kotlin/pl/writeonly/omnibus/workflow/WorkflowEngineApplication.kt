package pl.writeonly.omnibus.workflow

import io.camunda.client.annotation.Deployment
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@Deployment(resources = ["classpath*:/bpmn/*.bpmn"])
class WorkflowEngineApplication

fun main(args: Array<String>) {
    runApplication<WorkflowEngineApplication>(*args)
}
