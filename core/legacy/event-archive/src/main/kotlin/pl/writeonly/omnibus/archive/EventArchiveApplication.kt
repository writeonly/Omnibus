package pl.writeonly.omnibus.archive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class EventArchiveApplication

fun main(args: Array<String>) {
    runApplication<EventArchiveApplication>(*args)
}

