package com.omnibus.bidding.application

import com.omnibus.bidding.domain.RecommendationRequest
import com.omnibus.bidding.domain.RecommendationResponse
import com.omnibus.bidding.domain.HandParser
import com.omnibus.bidding.events.DomainEventPublisher
import com.omnibus.bidding.events.RecommendationProducedEvent
import com.omnibus.bidding.rules.BiddingFacts
import com.omnibus.bidding.rules.CandidateBid
import com.omnibus.bidding.rules.DroolsBiddingEngine
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Instant
import java.util.UUID

@Service
class BiddingRecommendationService(
    private val handParser: HandParser,
    private val droolsBiddingEngine: DroolsBiddingEngine,
    private val domainEventPublisher: DomainEventPublisher,
) {
    fun recommend(request: RecommendationRequest): Mono<RecommendationResponse> =
        Mono.fromCallable {
            val northHandProfile = handParser.parse(request.northHand)
            handParser.parse(request.southHand)
            val biddingFacts = BiddingFacts.from(northHandProfile, request.auction, request.system)
            val candidates = droolsBiddingEngine.evaluate(biddingFacts)
            val bestCandidate = candidates.maxByOrNull { it.priority }
                ?: CandidateBid("PASS", 0, "No matching rule found")

            val response = RecommendationResponse(
                request.system,
                "NORTH",
                northHandProfile.normalizedHand,
                request.southHand.trim().uppercase(),
                request.auction ?: "",
                bestCandidate.bid,
                "${bestCandidate.reason} Current MVP evaluates the opening decision for North.",
                candidates,
            )

            domainEventPublisher.publishRecommendationProduced(
                RecommendationProducedEvent(
                    UUID.randomUUID().toString(),
                    Instant.now(),
                    response.system,
                    response.evaluatedSeat,
                    response.northHand,
                    response.southHand,
                    response.auction,
                    response.recommendedBid,
                    response.explanation,
                    response.candidates.map { it.bid },
                ),
            )

            response
        }.subscribeOn(Schedulers.boundedElastic())
}

