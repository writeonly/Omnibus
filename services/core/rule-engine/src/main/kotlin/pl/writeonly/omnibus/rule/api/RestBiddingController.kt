package pl.writeonly.omnibus.rule.api

import pl.writeonly.omnibus.rule.application.RestBiddingService
import pl.writeonly.omnibus.rule.domain.RecommendationRequest
import pl.writeonly.omnibus.rule.domain.RecommendationResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/bidding")
class RestBiddingController(
    private val biddingRecommendationService: RestBiddingService,
) {
    @PostMapping("/recommend")
    fun recommend(@Valid @RequestBody request: RecommendationRequest): RecommendationResponse =
        biddingRecommendationService.recommend(request)

    @GetMapping("/health")
    fun health(): String = "OK"
}
