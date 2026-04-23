package pl.writeonly.omnibus.bidding.application

import com.omnibus.bidding.domain.RecommendationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(properties = ["omnibus.kafka.enabled=false"])
class BiddingRecommendationServiceTest {
    @Autowired
    private lateinit var biddingRecommendationService: BiddingRecommendationService

    @Test
    fun shouldRecommendOneNoTrumpForBalancedFifteenToSeventeenHand() {
        val response = biddingRecommendationService.recommend(
            RecommendationRequest("AQJ KQ2 A43 J742", "T97 A854 Q76 K98", "", "POLISH_CLUB"),
        ).block()

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("1NT")
    }

    @Test
    fun shouldRecommendPassBelowOpeningThreshold() {
        val response = biddingRecommendationService.recommend(
            RecommendationRequest("QJ4 T82 A743 942", "A83 KQ54 T62 K75", "", "POLISH_CLUB"),
        ).block()

        requireNotNull(response)
        assertThat(response.recommendedBid).isEqualTo("PASS")
    }
}

