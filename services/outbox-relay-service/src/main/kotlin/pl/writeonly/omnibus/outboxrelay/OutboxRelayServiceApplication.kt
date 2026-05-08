package pl.writeonly.omnibus.outboxrelay

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OutboxRelayServiceApplication

fun main(args: Array<String>) {
    runApplication<OutboxRelayServiceApplication>(*args)
}
