package pl.writeonly.omnibus.rule.api

import pl.writeonly.omnibus.rule.application.NextBidService
import pl.writeonly.omnibus.rule.domain.NextBidRequest
import pl.writeonly.omnibus.rule.domain.NextBidResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/next-bid")
class NextBidController(
    private val nextBidService: NextBidService,
) {
    @PostMapping
    fun nextBid(@Valid @RequestBody request: NextBidRequest): NextBidResponse =
        nextBidService.nextBid(request)
}
