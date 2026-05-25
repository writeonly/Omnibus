package pl.writeonly.omnibus.rule.api

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.writeonly.omnibus.rule.application.NextBidService

@RestController
@RequestMapping("/api/v1/health")
class HealthController(
    private val biddingRecommendationService: NextBidService,
) {
    fun health(): String = "OK"
}
