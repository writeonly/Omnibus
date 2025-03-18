package pl.writeonly.omnibus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OmnibusApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<OmnibusApplication>(*args)
}
