package pl.writeonly.omnibus.bidding.events

interface DomainEventPublisher {
    fun publishRecommendationProduced(event: RecommendationProducedEvent)

    fun publishRuleUpdated(event: RuleUpdatedEvent)
}

