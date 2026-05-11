package pl.writeonly.omnibus.bidding.application

import pl.writeonly.omnibus.bidding.domain.RecommendationRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationResponse
import pl.writeonly.omnibus.bidding.domain.HandParser
import pl.writeonly.omnibus.bidding.events.DomainEventPublisher
import pl.writeonly.omnibus.bidding.events.RecommendationProducedEvent
import pl.writeonly.omnibus.bidding.rules.BiddingFacts
import pl.writeonly.omnibus.bidding.rules.CandidateBid
import pl.writeonly.omnibus.bidding.rules.DroolsBiddingEngine
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class RestBiddingService(
    private val handParser: HandParser,
    private val droolsBiddingEngine: DroolsBiddingEngine,
    private val domainEventPublisher: DomainEventPublisher,
) {
    fun recommend(request: RecommendationRequest): RecommendationResponse {
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

        return response
    }
}
