package com.omnibus.archive.service;

import com.omnibus.archive.events.RecommendationProducedEvent;
import com.omnibus.archive.events.RuleUpdatedEvent;
import com.omnibus.archive.model.RecommendationByDay;
import com.omnibus.archive.model.RecommendationByDayKey;
import com.omnibus.archive.model.RuleUpdateByDay;
import com.omnibus.archive.model.RuleUpdateByDayKey;
import com.omnibus.archive.repository.RecommendationArchiveRepository;
import com.omnibus.archive.repository.RuleUpdateArchiveRepository;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventArchiveService {

    private final RecommendationArchiveRepository recommendationArchiveRepository;
    private final RuleUpdateArchiveRepository ruleUpdateArchiveRepository;

    public EventArchiveService(
        RecommendationArchiveRepository recommendationArchiveRepository,
        RuleUpdateArchiveRepository ruleUpdateArchiveRepository
    ) {
        this.recommendationArchiveRepository = recommendationArchiveRepository;
        this.ruleUpdateArchiveRepository = ruleUpdateArchiveRepository;
    }

    public Mono<Void> archiveRecommendation(RecommendationProducedEvent event) {
        return recommendationArchiveRepository.save(new RecommendationByDay(
                new RecommendationByDayKey(
                    event.occurredAt().atZone(ZoneOffset.UTC).toLocalDate(),
                    UUID.fromString(event.eventId())
                ),
                event.occurredAt(),
                event.system(),
                event.evaluatedSeat(),
                event.northHand(),
                event.southHand(),
                event.auction(),
                event.recommendedBid(),
                event.explanation(),
                event.candidateBids()
            ))
            .then();
    }

    public Mono<Void> archiveRuleUpdate(RuleUpdatedEvent event) {
        return ruleUpdateArchiveRepository.save(new RuleUpdateByDay(
                new RuleUpdateByDayKey(
                    event.occurredAt().atZone(ZoneOffset.UTC).toLocalDate(),
                    UUID.fromString(event.eventId())
                ),
                event.occurredAt(),
                event.ruleName(),
                event.sourcePath(),
                event.managed(),
                event.contentLength()
            ))
            .then();
    }
}
