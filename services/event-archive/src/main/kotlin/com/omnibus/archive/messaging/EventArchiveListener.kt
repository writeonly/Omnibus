package com.omnibus.archive.messaging

import com.omnibus.archive.events.RecommendationProducedEvent
import com.omnibus.archive.events.RuleUpdatedEvent
import com.omnibus.archive.service.EventArchiveService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class EventArchiveListener(
    private val eventArchiveService: EventArchiveService,
) {
    @KafkaListener(
        topics = ["\${omnibus.kafka.topics.recommendation-produced}"],
        groupId = "event-archive-recommendations",
        properties = ["spring.json.value.default.type=com.omnibus.archive.events.RecommendationProducedEvent"],
    )
    fun onRecommendationProduced(event: RecommendationProducedEvent) {
        eventArchiveService.archiveRecommendation(event).block()
    }

    @KafkaListener(
        topics = ["\${omnibus.kafka.topics.rule-updated}"],
        groupId = "event-archive-rule-updates",
        properties = ["spring.json.value.default.type=com.omnibus.archive.events.RuleUpdatedEvent"],
    )
    fun onRuleUpdated(event: RuleUpdatedEvent) {
        eventArchiveService.archiveRuleUpdate(event).block()
    }
}

