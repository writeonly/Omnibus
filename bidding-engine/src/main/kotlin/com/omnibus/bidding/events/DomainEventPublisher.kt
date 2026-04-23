package com.omnibus.bidding.events

interface DomainEventPublisher {
    fun publishRecommendationProduced(event: RecommendationProducedEvent)

    fun publishRuleUpdated(event: RuleUpdatedEvent)
}

