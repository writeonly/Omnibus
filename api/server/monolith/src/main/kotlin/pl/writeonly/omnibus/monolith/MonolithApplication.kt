package pl.writeonly.omnibus.monolith

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MonolithApplication {
}

fun main(args: Array<String>) {
    runApplication<MonolithApplication>(*args)
}