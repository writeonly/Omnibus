package com.omnibus.workflow.domain;

import jakarta.validation.constraints.NotBlank;

public record RulePublicationRequest(
    @NotBlank String name,
    @NotBlank String content,
    @NotBlank String requestedBy
) {
}
