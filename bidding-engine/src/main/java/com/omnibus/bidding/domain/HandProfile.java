package com.omnibus.bidding.domain;

public record HandProfile(
    String normalizedHand,
    int spades,
    int hearts,
    int diamonds,
    int clubs,
    int hcp,
    boolean balanced
) {
}
