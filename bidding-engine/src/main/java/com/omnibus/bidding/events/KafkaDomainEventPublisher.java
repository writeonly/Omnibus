package com.omnibus.bidding.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "omnibus.kafka.enabled", havingValue = "true", matchIfMissing = true)
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDomainEventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaTopicsProperties topics;

    public KafkaDomainEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, KafkaTopicsProperties topics) {
        this.kafkaTemplate = kafkaTemplate;
        this.topics = topics;
    }

    @Override
    public void publishRecommendationProduced(RecommendationProducedEvent event) {
        kafkaTemplate.send(topics.recommendationProduced(), event.eventId(), event)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    LOGGER.warn("Failed to publish recommendation event {}", event.eventId(), throwable);
                }
            });
    }

    @Override
    public void publishRuleUpdated(RuleUpdatedEvent event) {
        kafkaTemplate.send(topics.ruleUpdated(), event.eventId(), event)
            .whenComplete((result, throwable) -> {
                if (throwable != null) {
                    LOGGER.warn("Failed to publish rule update event {}", event.eventId(), throwable);
                }
            });
    }
}
