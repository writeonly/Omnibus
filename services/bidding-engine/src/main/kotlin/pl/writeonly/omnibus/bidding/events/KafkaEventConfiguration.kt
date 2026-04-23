package pl.writeonly.omnibus.bidding.events

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(KafkaTopicsProperties::class)
class KafkaEventConfiguration {
    @Bean
    @ConditionalOnProperty(name = ["omnibus.kafka.enabled"], havingValue = "true", matchIfMissing = true)
    fun recommendationProducedTopic(topics: KafkaTopicsProperties): NewTopic =
        NewTopic(topics.recommendationProduced, 1, 1.toShort())

    @Bean
    @ConditionalOnProperty(name = ["omnibus.kafka.enabled"], havingValue = "true", matchIfMissing = true)
    fun ruleUpdatedTopic(topics: KafkaTopicsProperties): NewTopic =
        NewTopic(topics.ruleUpdated, 1, 1.toShort())
}

