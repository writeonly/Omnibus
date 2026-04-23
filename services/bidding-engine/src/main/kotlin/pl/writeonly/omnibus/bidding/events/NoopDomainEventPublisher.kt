package pl.writeonly.omnibus.bidding.events

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(name = ["omnibus.kafka.enabled"], havingValue = "false")
class NoopDomainEventPublisher : DomainEventPublisher {
    override fun publishRecommendationProduced(event: RecommendationProducedEvent) {
        // intentionally no-op
    }

    override fun publishRuleUpdated(event: RuleUpdatedEvent) {
        // intentionally no-op
    }
}

