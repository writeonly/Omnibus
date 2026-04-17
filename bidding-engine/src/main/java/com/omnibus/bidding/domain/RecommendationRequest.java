package com.omnibus.bidding.domain;

import jakarta.validation.constraints.NotBlank;

public record RecommendationRequest(
    @NotBlank String hand,
    String auction,
    @NotBlank String system
) {
}
