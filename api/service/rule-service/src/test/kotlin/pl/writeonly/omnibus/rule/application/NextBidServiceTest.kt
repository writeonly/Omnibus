package pl.writeonly.omnibus.rule.application

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import pl.writeonly.omnibus.rule.domain.NextBidRequest
import pl.writeonly.omnibus.rule.domain.HandParser
import pl.writeonly.omnibus.rule.events.DomainEventPublisher
import pl.writeonly.omnibus.rule.events.NextBidProducedEvent
import pl.writeonly.omnibus.rule.events.RuleUpdatedEvent
import pl.writeonly.omnibus.rule.rules.DroolsBiddingEngine
import pl.writeonly.omnibus.rule.rules.DroolsCompilationService
import pl.writeonly.omnibus.rule.rules.RuleCatalogService

class NextBidServiceTest : FreeSpec({
    val nextBidService = NextBidService(
        HandParser(),
        DroolsBiddingEngine(
            RuleCatalogService(ManagedRuleStore("build/tmp/managed-rules-test")),
            DroolsCompilationService(),
        ),
        object : DomainEventPublisher {
            override fun publishRecommendationProduced(event: NextBidProducedEvent) = Unit

            override fun publishRuleUpdated(event: RuleUpdatedEvent) = Unit
        },
    )

    "nextBid" - {
        "recommends 1NT for a balanced 15-17 HCP hand" {
            val response = nextBidService.nextBid(
                NextBidRequest("AQJ KQ2 A43 J742", "T97 A854 Q76 K98", "", "POLISH_CLUB"),
            )

            response.recommendedBid shouldBe "1NT"
        }

        "recommends PASS below the opening threshold" {
            val response = nextBidService.nextBid(
                NextBidRequest("QJ4 T82 A743 942", "A83 KQ54 T62 K75", "", "POLISH_CLUB"),
            )

            response.recommendedBid shouldBe "PASS"
        }
    }
})
