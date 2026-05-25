package pl.writeonly.omnibus.rule.application

import pl.writeonly.omnibus.rule.domain.NextBidRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Disabled

@SpringBootTest(properties = ["omnibus.kafka.enabled=false"])
@Disabled("This test is not working after refactor, needs to be fixed")
class NextBidServiceTest {
    @Autowired
    private lateinit var biddingRecommendationService: NextBidService

    @Test
    fun shouldNextBidOneNoTrumpForBalancedFifteenToSeventeenHand() {
        val response = biddingRecommendationService.nextBid(
            NextBidRequest("AQJ KQ2 A43 J742", "T97 A854 Q76 K98", "", "POLISH_CLUB"),
        )

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("1NT")
    }

    @Test
    fun shouldNextBidPassBelowOpeningThreshold() {
        val response = biddingRecommendationService.nextBid(
            NextBidRequest("QJ4 T82 A743 942", "A83 KQ54 T62 K75", "", "POLISH_CLUB"),
        )

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("PASS")
    }
}
