package com.omnibus.bidding.api;

import com.omnibus.bidding.application.BiddingRecommendationService;
import com.omnibus.bidding.domain.RecommendationRequest;
import com.omnibus.bidding.domain.RecommendationResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/bidding")
public class BiddingRecommendationController {

    private final BiddingRecommendationService biddingRecommendationService;

    public BiddingRecommendationController(BiddingRecommendationService biddingRecommendationService) {
        this.biddingRecommendationService = biddingRecommendationService;
    }

    @PostMapping("/recommend")
    public Mono<RecommendationResponse> recommend(@Valid @RequestBody RecommendationRequest request) {
        return biddingRecommendationService.recommend(request);
    }

    @GetMapping("/health")
    public Mono<String> health() {
        return Mono.just("OK");
    }
}
