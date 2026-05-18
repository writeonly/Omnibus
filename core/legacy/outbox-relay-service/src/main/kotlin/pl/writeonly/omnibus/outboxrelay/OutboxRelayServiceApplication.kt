package pl.writeonly.omnibus.outboxrelay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class OutboxRelayServiceApplication

fun main(args: Array<String>) {
    runApplication<OutboxRelayServiceApplication>(*args)
}
