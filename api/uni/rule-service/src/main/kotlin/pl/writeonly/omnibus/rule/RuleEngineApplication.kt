package pl.writeonly.omnibus.rule

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class RuleEngineApplication

fun main(args: Array<String>) {
    runApplication<RuleEngineApplication>(*args)
}

