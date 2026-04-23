package com.omnibus.bidding.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.omnibus.bidding.domain.RecommendationRequest;
import com.omnibus.bidding.domain.RecommendationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "omnibus.kafka.enabled=false")
class BiddingRecommendationServiceTest {

    @Autowired
    private BiddingRecommendationService biddingRecommendationService;

    @Test
    void shouldRecommendOneNoTrumpForBalancedFifteenToSeventeenHand() {
        RecommendationResponse response = biddingRecommendationService.recommend(
            new RecommendationRequest("AQJ KQ2 A43 J742", "T97 A854 Q76 K98", "", "POLISH_CLUB")
        ).block();

        assertThat(response.recommendedBid()).isEqualTo("1NT");
    }

    @Test
    void shouldRecommendPassBelowOpeningThreshold() {
        RecommendationResponse response = biddingRecommendationService.recommend(
            new RecommendationRequest("QJ4 T82 A743 942", "A83 KQ54 T62 K75", "", "POLISH_CLUB")
        ).block();

        assertThat(response.recommendedBid()).isEqualTo("PASS");
    }
}
