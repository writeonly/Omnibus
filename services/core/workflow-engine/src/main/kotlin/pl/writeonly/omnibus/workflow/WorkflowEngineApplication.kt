package pl.writeonly.omnibus.workflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WorkflowEngineApplication

fun main(args: Array<String>) {
    runApplication<WorkflowEngineApplication>(*args)
}
