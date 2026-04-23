package pl.writeonly.omnibus.bidding

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BiddingEngineApplication

fun main(args: Array<String>) {
    runApplication<BiddingEngineApplication>(*args)
}

