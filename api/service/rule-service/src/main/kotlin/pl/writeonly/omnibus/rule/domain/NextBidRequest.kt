package pl.writeonly.omnibus.rule.domain

import jakarta.validation.constraints.NotBlank

data class NextBidRequest(
    @field:NotBlank val northHand: String,
    @field:NotBlank val southHand: String,
    val auction: String?,
    @field:NotBlank val system: String,
)

