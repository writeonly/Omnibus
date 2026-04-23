package pl.writeonly.omnibus.bidding.api

import com.omnibus.bidding.application.BiddingRecommendationService
import com.omnibus.bidding.domain.RecommendationRequest
import com.omnibus.bidding.domain.RecommendationResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/bidding")
class BiddingRecommendationController(
    private val biddingRecommendationService: BiddingRecommendationService,
) {
    @PostMapping("/recommend")
    fun recommend(@Valid @RequestBody request: RecommendationRequest): Mono<RecommendationResponse> =
        biddingRecommendationService.recommend(request)

    @GetMapping("/health")
    fun health(): Mono<String> = Mono.just("OK")
}

