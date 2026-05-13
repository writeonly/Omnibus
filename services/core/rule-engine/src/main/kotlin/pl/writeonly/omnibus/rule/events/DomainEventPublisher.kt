package pl.writeonly.omnibus.rule.events

interface DomainEventPublisher {
    fun publishRecommendationProduced(event: RecommendationProducedEvent)

    fun publishRuleUpdated(event: RuleUpdatedEvent)
}

