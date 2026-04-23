package com.omnibus.archive.messaging;

import com.omnibus.archive.config.KafkaTopicsProperties;
import com.omnibus.archive.events.RecommendationProducedEvent;
import com.omnibus.archive.events.RuleUpdatedEvent;
import com.omnibus.archive.service.EventArchiveService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EventArchiveListener {

    private final EventArchiveService eventArchiveService;

    public EventArchiveListener(EventArchiveService eventArchiveService) {
        this.eventArchiveService = eventArchiveService;
    }

    @KafkaListener(
        topics = "${omnibus.kafka.topics.recommendation-produced}",
        groupId = "event-archive-recommendations",
        properties = {
            "spring.json.value.default.type=com.omnibus.archive.events.RecommendationProducedEvent"
        }
    )
    public void onRecommendationProduced(RecommendationProducedEvent event) {
        eventArchiveService.archiveRecommendation(event);
    }

    @KafkaListener(
        topics = "${omnibus.kafka.topics.rule-updated}",
        groupId = "event-archive-rule-updates",
        properties = {
            "spring.json.value.default.type=com.omnibus.archive.events.RuleUpdatedEvent"
        }
    )
    public void onRuleUpdated(RuleUpdatedEvent event) {
        eventArchiveService.archiveRuleUpdate(event);
    }
}
