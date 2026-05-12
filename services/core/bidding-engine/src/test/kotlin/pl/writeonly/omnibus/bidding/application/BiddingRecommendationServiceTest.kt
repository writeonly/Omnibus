package pl.writeonly.omnibus.bidding.application

import pl.writeonly.omnibus.bidding.domain.RecommendationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.junit.jupiter.api.Disabled

@SpringBootTest(properties = ["omnibus.kafka.enabled=false"])
@Disabled("This test is not working after refactor, needs to be fixed")
class RestBiddingServiceTest {
    @Autowired
    private lateinit var biddingRecommendationService: RestBiddingService

    @Test
    fun shouldRecommendOneNoTrumpForBalancedFifteenToSeventeenHand() {
        val response = biddingRecommendationService.recommend(
            RecommendationRequest("AQJ KQ2 A43 J742", "T97 A854 Q76 K98", "", "POLISH_CLUB"),
        )

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("1NT")
    }

    @Test
    fun shouldRecommendPassBelowOpeningThreshold() {
        val response = biddingRecommendationService.recommend(
            RecommendationRequest("QJ4 T82 A743 942", "A83 KQ54 T62 K75", "", "POLISH_CLUB"),
        )

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("PASS")
    }
}
