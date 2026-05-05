package pl.writeonly.omnibus.bidding.api

import pl.writeonly.omnibus.bidding.application.RestBiddingationService
import pl.writeonly.omnibus.bidding.domain.RecommendationRequest
import pl.writeonly.omnibus.bidding.domain.RecommendationResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bidding")
class RestBiddingationController(
    private val biddingRecommendationService: RestBiddingationService,
) {
    @PostMapping("/recommend")
    fun recommend(@Valid @RequestBody request: RecommendationRequest): RecommendationResponse =
        biddingRecommendationService.recommend(request)

    @GetMapping("/health")
    fun health(): String = "OK"
}
