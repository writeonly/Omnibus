package com.omnibus.bidding.events;

public interface DomainEventPublisher {

    void publishRecommendationProduced(RecommendationProducedEvent event);

    void publishRuleUpdated(RuleUpdatedEvent event);
}
