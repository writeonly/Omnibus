package com.omnibus.bidding.domain;

import jakarta.validation.constraints.NotBlank;

public record RecommendationRequest(
    @NotBlank String northHand,
    @NotBlank String southHand,
    String auction,
    @NotBlank String system
) {
}
