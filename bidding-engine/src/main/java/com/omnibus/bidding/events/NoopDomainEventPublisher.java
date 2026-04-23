package com.omnibus.bidding.events;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "omnibus.kafka.enabled", havingValue = "false")
public class NoopDomainEventPublisher implements DomainEventPublisher {

    @Override
    public void publishRecommendationProduced(RecommendationProducedEvent event) {
    }

    @Override
    public void publishRuleUpdated(RuleUpdatedEvent event) {
    }
}
