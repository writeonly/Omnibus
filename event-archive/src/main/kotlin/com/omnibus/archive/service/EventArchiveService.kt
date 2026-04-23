package com.omnibus.archive.service

import com.omnibus.archive.events.RecommendationProducedEvent
import com.omnibus.archive.events.RuleUpdatedEvent
import com.omnibus.archive.model.RecommendationByDay
import com.omnibus.archive.model.RecommendationByDayKey
import com.omnibus.archive.model.RuleUpdateByDay
import com.omnibus.archive.model.RuleUpdateByDayKey
import com.omnibus.archive.repository.RecommendationArchiveRepository
import com.omnibus.archive.repository.RuleUpdateArchiveRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.ZoneOffset
import java.util.UUID

@Service
class EventArchiveService(
    private val recommendationArchiveRepository: RecommendationArchiveRepository,
    private val ruleUpdateArchiveRepository: RuleUpdateArchiveRepository,
) {
    fun archiveRecommendation(event: RecommendationProducedEvent): Mono<Void> =
        recommendationArchiveRepository.save(
            RecommendationByDay(
                RecommendationByDayKey(
                    event.occurredAt.atZone(ZoneOffset.UTC).toLocalDate(),
                    UUID.fromString(event.eventId),
                ),
                event.occurredAt,
                event.system,
                event.evaluatedSeat,
                event.northHand,
                event.southHand,
                event.auction,
                event.recommendedBid,
                event.explanation,
                event.candidateBids,
            ),
        ).then()

    fun archiveRuleUpdate(event: RuleUpdatedEvent): Mono<Void> =
        ruleUpdateArchiveRepository.save(
            RuleUpdateByDay(
                RuleUpdateByDayKey(
                    event.occurredAt.atZone(ZoneOffset.UTC).toLocalDate(),
                    UUID.fromString(event.eventId),
                ),
                event.occurredAt,
                event.ruleName,
                event.sourcePath,
                event.managed,
                event.contentLength,
            ),
        ).then()
}

