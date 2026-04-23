package com.omnibus.bidding.domain;

public record ManagedRuleDefinition(
    String name,
    String sourcePath,
    boolean managed,
    String content
) {
}
