package pl.writeonly.omnibus.archive.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "omnibus.kafka.topics")
data class KafkaTopicsProperties(
    val recommendationProduced: String,
    val ruleUpdated: String,
    val bffOutboxEvents: String,
)
