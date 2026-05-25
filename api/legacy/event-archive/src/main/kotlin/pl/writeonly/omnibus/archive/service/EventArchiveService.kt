package pl.writeonly.omnibus.archive.service

import com.fasterxml.jackson.databind.ObjectMapper
import pl.writeonly.omnibus.archive.events.BffHttpOutboxEvent
import pl.writeonly.omnibus.archive.events.RecommendationProducedEvent
import pl.writeonly.omnibus.archive.events.RuleUpdatedEvent
import pl.writeonly.omnibus.archive.model.BffHttpEventByDay
import pl.writeonly.omnibus.archive.model.BffHttpEventByDayKey
import pl.writeonly.omnibus.archive.model.RecommendationByDay
import pl.writeonly.omnibus.archive.model.RecommendationByDayKey
import pl.writeonly.omnibus.archive.model.RuleUpdateByDay
import pl.writeonly.omnibus.archive.model.RuleUpdateByDayKey
import pl.writeonly.omnibus.archive.repository.BffHttpEventArchiveRepository
import pl.writeonly.omnibus.archive.repository.RecommendationArchiveRepository
import pl.writeonly.omnibus.archive.repository.RuleUpdateArchiveRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.time.ZoneOffset
import java.util.UUID

@Service
class EventArchiveService(
    private val recommendationArchiveRepository: RecommendationArchiveRepository,
    private val ruleUpdateArchiveRepository: RuleUpdateArchiveRepository,
    private val bffHttpEventArchiveRepository: BffHttpEventArchiveRepository,
    private val objectMapper: ObjectMapper,
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

    fun archiveBffHttpEvent(event: BffHttpOutboxEvent): Mono<Void> =
        bffHttpEventArchiveRepository.save(
            BffHttpEventByDay(
                BffHttpEventByDayKey(
                    event.occurredAt.atZone(ZoneOffset.UTC).toLocalDate(),
                    UUID.fromString(event.eventId),
                ),
                event.occurredAt,
                event.eventType,
                event.aggregateType,
                event.aggregateId,
                event.method,
                event.path,
                event.statusCode,
                event.durationMs,
                objectMapper.writeValueAsString(event.request),
                event.response?.let(objectMapper::writeValueAsString),
                event.error?.let(objectMapper::writeValueAsString),
            ),
        ).then()
}
