package pl.writeonly.omnibus.rule

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BiddingEngineApplication

fun main(args: Array<String>) {
    runApplication<BiddingEngineApplication>(*args)
}

