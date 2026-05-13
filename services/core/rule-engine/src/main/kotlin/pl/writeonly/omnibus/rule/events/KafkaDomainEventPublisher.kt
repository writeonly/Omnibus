package pl.writeonly.omnibus.rule.events

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["omnibus.kafka.enabled"], havingValue = "true", matchIfMissing = true)
class KafkaDomainEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
    private val topics: KafkaTopicsProperties,
) : DomainEventPublisher {
    override fun publishRecommendationProduced(event: RecommendationProducedEvent) {
        kafkaTemplate.send(topics.recommendationProduced, event.eventId, event)
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    LOGGER.warn("Failed to publish recommendation event {}", event.eventId, throwable)
                }
            }
    }

    override fun publishRuleUpdated(event: RuleUpdatedEvent) {
        kafkaTemplate.send(topics.ruleUpdated, event.eventId, event)
            .whenComplete { _, throwable ->
                if (throwable != null) {
                    LOGGER.warn("Failed to publish rule update event {}", event.eventId, throwable)
                }
            }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(KafkaDomainEventPublisher::class.java)
    }
}

