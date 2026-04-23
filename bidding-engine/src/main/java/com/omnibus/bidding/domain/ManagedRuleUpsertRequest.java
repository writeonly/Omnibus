package com.omnibus.bidding.domain;

import jakarta.validation.constraints.NotBlank;

public record ManagedRuleUpsertRequest(
    @NotBlank String name,
    @NotBlank String content
) {
}
