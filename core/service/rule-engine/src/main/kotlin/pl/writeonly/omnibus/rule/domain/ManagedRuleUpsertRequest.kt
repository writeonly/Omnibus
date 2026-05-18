package pl.writeonly.omnibus.rule.domain

import jakarta.validation.constraints.NotBlank

data class ManagedRuleUpsertRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val content: String,
)

