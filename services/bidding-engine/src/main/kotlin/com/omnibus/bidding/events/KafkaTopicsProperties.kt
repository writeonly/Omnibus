package com.omnibus.bidding.events

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "omnibus.kafka.topics")
data class KafkaTopicsProperties(
    val recommendationProduced: String,
    val ruleUpdated: String,
)

