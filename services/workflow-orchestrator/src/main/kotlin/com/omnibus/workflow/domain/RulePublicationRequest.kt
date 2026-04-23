package com.omnibus.workflow.domain

import jakarta.validation.constraints.NotBlank

data class RulePublicationRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val content: String,
    @field:NotBlank val requestedBy: String,
)

