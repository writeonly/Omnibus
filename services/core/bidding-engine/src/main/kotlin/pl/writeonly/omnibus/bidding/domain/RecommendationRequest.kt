package pl.writeonly.omnibus.bidding.domain

import jakarta.validation.constraints.NotBlank

data class RecommendationRequest(
    @field:NotBlank val northHand: String,
    @field:NotBlank val southHand: String,
    val auction: String?,
    @field:NotBlank val system: String,
)

