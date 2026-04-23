package com.omnibus.bidding.domain

data class ManagedRuleDefinition(
    val name: String,
    val sourcePath: String,
    val managed: Boolean,
    val content: String,
)

