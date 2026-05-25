package pl.writeonly.omnibus.rule.application

import pl.writeonly.omnibus.rule.domain.NextBidRequest
import pl.writeonly.omnibus.rule.domain.NextBidResponse
import pl.writeonly.omnibus.rule.domain.HandParser
import pl.writeonly.omnibus.rule.events.DomainEventPublisher
import pl.writeonly.omnibus.rule.events.NextBidProducedEvent
import pl.writeonly.omnibus.rule.rules.BiddingFacts
import pl.writeonly.omnibus.rule.rules.CandidateBid
import pl.writeonly.omnibus.rule.rules.DroolsBiddingEngine
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.UUID

@Service
class NextBidService(
    private val handParser: HandParser,
    private val droolsBiddingEngine: DroolsBiddingEngine,
    private val domainEventPublisher: DomainEventPublisher,
) {
    fun nextBid(request: NextBidRequest): NextBidResponse {
        val northHandProfile = handParser.parse(request.northHand)
        handParser.parse(request.southHand)
        val biddingFacts = BiddingFacts.from(northHandProfile, request.auction, request.system)
        val candidates = droolsBiddingEngine.evaluate(biddingFacts)
        val bestCandidate = candidates.maxByOrNull { it.priority }
            ?: CandidateBid("PASS", 0, "No matching rule found")

        val response = NextBidResponse(
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
            NextBidProducedEvent(
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
