package com.omnibus.bidding.events;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties.class)
public class KafkaEventConfiguration {

    @Bean
    @ConditionalOnProperty(name = "omnibus.kafka.enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic recommendationProducedTopic(KafkaTopicsProperties topics) {
        return new NewTopic(topics.recommendationProduced(), 1, (short) 1);
    }

    @Bean
    @ConditionalOnProperty(name = "omnibus.kafka.enabled", havingValue = "true", matchIfMissing = true)
    public NewTopic ruleUpdatedTopic(KafkaTopicsProperties topics) {
        return new NewTopic(topics.ruleUpdated(), 1, (short) 1);
    }
}
