package com.omnibus.archive.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "omnibus.kafka.topics")
public record KafkaTopicsProperties(
    String recommendationProduced,
    String ruleUpdated
) {
}
