package pl.writeonly.omnibus

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OmbibusApplication

fun main(args: Array<String>) {
    runApplication<OmbibusApplication>(*args)
}
