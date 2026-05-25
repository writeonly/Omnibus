package pl.writeonly.omnibus.rule.events

interface DomainEventPublisher {
    fun publishRecommendationProduced(event: NextBidProducedEvent)

    fun publishRuleUpdated(event: RuleUpdatedEvent)
}

