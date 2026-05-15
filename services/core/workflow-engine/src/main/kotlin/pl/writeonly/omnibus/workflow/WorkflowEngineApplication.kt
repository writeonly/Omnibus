package pl.writeonly.omnibus.workflow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class WorkflowEngineApplication

fun main(args: Array<String>) {
    runApplication<WorkflowEngineApplication>(*args)
}
