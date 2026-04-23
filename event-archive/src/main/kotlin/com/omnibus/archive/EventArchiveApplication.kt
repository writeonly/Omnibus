package com.omnibus.archive

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventArchiveApplication

fun main(args: Array<String>) {
    runApplication<EventArchiveApplication>(*args)
}

